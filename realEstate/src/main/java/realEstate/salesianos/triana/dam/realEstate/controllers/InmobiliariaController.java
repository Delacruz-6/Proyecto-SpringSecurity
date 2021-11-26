package realEstate.salesianos.triana.dam.realEstate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import realEstate.salesianos.triana.dam.realEstate.dtos.*;
import realEstate.salesianos.triana.dam.realEstate.models.Inmobiliaria;
import realEstate.salesianos.triana.dam.realEstate.services.InmobiliariaService;
import org.springframework.web.bind.annotation.*;
import realEstate.salesianos.triana.dam.realEstate.users.models.UserRole;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;
import realEstate.salesianos.triana.dam.realEstate.users.services.UsuarioService;
import realEstate.salesianos.triana.dam.realEstate.util.PaginationLinksUtil;
import realEstate.salesianos.triana.dam.realEstate.services.ViviendaService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/inmobiliaria")
@RequiredArgsConstructor
public class InmobiliariaController {

    private final PaginationLinksUtil paginationLinksUtil;
    private final InmobiliariaService inmobiliariaService;
    private final InmobiliariaDtoConverter inmobiliariaDtoConverter;
    private final ViviendaService viviendaService;
    private final UsuarioService usuarioService;


    @Operation(summary = "Listar todas las inmobiliarias existentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se listan todas las inmobiliarias",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inmobiliaria.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna inmobiliaria.",
                    content = @Content),
    })
    @GetMapping("/")
    public ResponseEntity<?> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                     HttpServletRequest request) {
        Page<Inmobiliaria> data = inmobiliariaService.findAll(pageable);

        if(data.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            Page<GetInmobiliariaDto> result = data.map(inmobiliariaDtoConverter::inmobiliariaToGetInmobiliariaDto);

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
            return ResponseEntity.ok().header("link", paginationLinksUtil.createLinkHeader(result, uriBuilder)).body(result);
        }
    }

    @Operation(summary = "Mostrar los detalles de una inmobiliaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Se ha encontrado la inmobiliaria correctamente",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Inmobiliaria.class))}),
            @ApiResponse(responseCode = "404",
            description = "No se ha encontrado la inmobiliria",
            content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<GetInmobiliariaGestorDetailDto>> findOne(@PathVariable Long id){

        Optional<Inmobiliaria> inmo = inmobiliariaService.findById(id);
        if (inmobiliariaService.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else{
            List<GetInmobiliariaGestorDetailDto> inmobiliariaDto = inmo.stream()
                    .map(inmobiliariaDtoConverter::inmobiliariaToGetInmobiliariaViviendasDtoDetail)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(inmobiliariaDto);
        }
    }
    @Operation(summary = "Borrar una inmobiliria por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
            description = "Se ha borrado correctamente la inmobiliaria",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Inmobiliaria.class))}),
            @ApiResponse(responseCode = "404",
            description = "No se ha encontrado la inmobiliaria",
            content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,@AuthenticationPrincipal Usuario usuario){
        if (inmobiliariaService.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        } else if(usuario.getRol().equals(UserRole.ADMIN)){
            Optional<Inmobiliaria> inmo = inmobiliariaService.findById(id);
            Inmobiliaria inmo1 = inmo.get();
            inmo1.nullearInmobiliariaDeViviendas();
            inmobiliariaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }else{
            return new ResponseEntity<Inmobiliaria>(HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "Borrar un gestor de una inmobiliria por su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado el gestor correctamente de la inmobiliaria",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inmobiliaria.class))}),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @DeleteMapping("/gestor/{id}")
    public ResponseEntity<?> deleteGestor(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario){
        Optional<Inmobiliaria> inmobiliariaOptional = inmobiliariaService.findById(id);
        List<Usuario> gestores= inmobiliariaOptional.get().getGestores();
        if (inmobiliariaService.findById(id).isEmpty() || inmobiliariaService.findById(id).get().getGestores().isEmpty()){
            return ResponseEntity.notFound().build();
        }else if (usuario.getRol().equals(UserRole.ADMIN) ||
                !gestores.stream().filter(g -> g.getRol().equals(UserRole.GESTOR)).findFirst().isEmpty()){
            Optional<Inmobiliaria> inmo = inmobiliariaService.findById(id);
            Inmobiliaria inmo1 = inmo.get();
            if (inmo.isPresent()){
                //inmo1.nullearInmobiliariaDeGestores();
                //usuarioService.deleteById(id);
                //inmobiliariaService.findAll().
            }
            return ResponseEntity.noContent().build();
        } else{
            return new ResponseEntity<Inmobiliaria>(HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "Agregar una nueva inmobiliaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado la inmobiliaria correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inmobiliaria.class))}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha podido crear la inmobiliaria, sintáxis inválida",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content),
    })
    @PostMapping("/")
    public ResponseEntity<Inmobiliaria> create(@RequestBody Inmobiliaria inmobiliaria, @AuthenticationPrincipal Usuario usuario) {
        if (usuario.getRol().equals(UserRole.ADMIN)){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(inmobiliariaService.save(inmobiliaria));
    }else{
            return new ResponseEntity<Inmobiliaria>(HttpStatus.FORBIDDEN);
        }
    }


    @Operation(summary = "Agregar un nuevo gestor a una inmobiliaria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha añadido un gestor a una inmobiliaria correctamente",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inmobiliaria.class))}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha podido crear el gestor en inmobiliaria, sintáxis inválida",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content),
    })
    @PostMapping("/{id}/gestor")
    public ResponseEntity<GetInmobiliariaGestorPostDto> createGestor(@RequestBody CreateGestorDto dtoGestor , @PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        Optional<Inmobiliaria> inmobiliariaOptional = inmobiliariaService.findById(id);
        if(inmobiliariaOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }else if (usuario.getRol().equals(UserRole.ADMIN) ||
                ((usuario.getRol().equals(UserRole.GESTOR) && (inmobiliariaOptional.get().getId().equals(usuario.getInmobiliaria().getId()))))) {
            Inmobiliaria inmobiliaria = inmobiliariaOptional.get();
            Usuario gestor = Usuario.builder()
                    .id(dtoGestor.getIdGestor())
                    .build();
            Optional<Usuario> usuariodataOptional= usuarioService.findById(dtoGestor.getIdGestor());
            Usuario usuarioData = usuariodataOptional.get();
            usuarioData.addInmobiliaria(inmobiliaria);
            usuarioService.save(usuarioData);
            inmobiliariaService.save(inmobiliaria);
            GetInmobiliariaGestorPostDto iDto = inmobiliariaDtoConverter.inmobiliariaToGetInmobiliariaGestorDtoPost(gestor,inmobiliaria);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(iDto);

        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


}
