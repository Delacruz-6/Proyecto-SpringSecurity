package realEstate.salesianos.triana.dam.realEstate.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import realEstate.salesianos.triana.dam.realEstate.dtos.*;
import realEstate.salesianos.triana.dam.realEstate.models.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import realEstate.salesianos.triana.dam.realEstate.repositories.ViviendaRepository;
import realEstate.salesianos.triana.dam.realEstate.services.*;
import realEstate.salesianos.triana.dam.realEstate.users.models.UserRole;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;
import realEstate.salesianos.triana.dam.realEstate.users.services.UsuarioService;
import realEstate.salesianos.triana.dam.realEstate.util.PaginationLinksUtil;


import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vivienda")
@RequiredArgsConstructor
public class ViviendaController {

    private final ViviendaService viviendaService;
    private final InmobiliariaService inmobiliariaService;
      private final ViviendaDtoConverter viviendaDtoConverter;
      private final PaginationLinksUtil paginationLinksUtil;
      private final UsuarioService usuarioService;
      private final InteresaService interesaService;
      private final InteresadoDtoConverter propietarioDtoConverter;
      private final PropietarioDtoConverter propietarioDtoConver;

    @Operation(summary = "Se crea una vivienda y si el propietario no existe, tambi??n lo crea")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se crea una vivienda con ??xito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "400",
                    description = "La estructura de la petici??n estaba mal formulada",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<?> createVivienda(@RequestBody Vivienda vivienda, @AuthenticationPrincipal Usuario usuario) {
        Vivienda saved = viviendaService.save(vivienda);
        //Vivienda saved =viviendaService.saveHouse(vivienda);
        if(!usuario.getRol().equals(UserRole.PROPIETARIO)){
            return new ResponseEntity<Vivienda>(HttpStatus.FORBIDDEN); /*GetViviendaDetailDto */
        }else{
            saved.addToPropietario(usuario);
            viviendaService.save(saved);

            //saved.setInmobiliaria(null);
            //GetViviendaDetailDto result = saved.map(viviendaDtoConverter::viviendaToGetViviendaDto);

        }
        return ResponseEntity.ok(viviendaDtoConverter.viviendaToGetViviendaDetailDto(saved));
    }

    @Operation(summary = "Eliminaci??n de una vivienda por su id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Borrar vivienda con ??xito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado una vivienda con ese id.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        Usuario propietario = usuarioService.findPropietario(id).get();
        if(viviendaService.findById(id).isEmpty() && viviendaService.findPropietario(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }else if( usuario.getRol().equals(UserRole.ADMIN) ||
                (usuario.getRol().equals(propietario.getRol()) &&
                        usuario.getId().equals(propietario.getId()))){
            viviendaService.findById(id).get().removeViviendasToIntereses();
            viviendaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
    }

    @Operation(summary = "Eliminaci??n de la inmobiliaria asociada a la vivienda seg??n su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Borrado de la inmobiliaria con ??xito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado una inmobiliaria en esta vivienda con ese id.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content)
    })
    @DeleteMapping("/{id1}/inmobiliaria")
    public ResponseEntity<?> deleteInmobiliariaToVivienda(@PathVariable Long id1, @AuthenticationPrincipal Usuario usuario) {

        Optional<Vivienda> viviendaOptional=viviendaService.findById(id1);
        Usuario propietario = usuarioService.findPropietario(id1).get();
        List<Usuario> gestores = viviendaService.findById(id1).get().getInmobiliaria().getGestores();

        if (viviendaOptional.isEmpty() && viviendaService.findPropietario(id1).isEmpty()){
            return ResponseEntity.notFound().build();
        }else if( usuario.getRol().equals(UserRole.ADMIN) || !gestores.stream().filter(gestor -> gestor.getRol().equals(UserRole.GESTOR)).findFirst().isEmpty() ||
                (usuario.getRol().equals(propietario.getRol()) &&
                        usuario.getId().equals(propietario.getId()))){
            Vivienda vivienda = viviendaOptional.get();
            Inmobiliaria inmobiliaria = new Inmobiliaria();
            vivienda.setInmobiliaria(inmobiliaria);
            vivienda.removeFromInmobiliaria(inmobiliaria);
            viviendaService.save(vivienda);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

@Operation(summary = "Listar todas las viviendas existentes con paginaci??n y filtrado")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "Se listan todas las viviendas",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = Vivienda.class))}),
        @ApiResponse(responseCode = "404",
                description = "No se ha encontrado ninguna vivienda",
                content = @Content),
        @ApiResponse(responseCode = "401",
                description = "Usuario no autenticado",
                content = @Content),
})
    @GetMapping("/")
    public ResponseEntity<?> buscarViviendaParametros (
            @RequestParam("ciudad") Optional<String> ciudad,
            @RequestParam("precioMax") Optional<Float> precioMax,
            @RequestParam("precioMin") Optional<Float> precioMin,
            @RequestParam("provincia") Optional<String> provincia,
            @RequestParam("tamanio") Optional<Float> tamanio,
            @RequestParam("numHabs") Optional<Float> numHabs,
            @RequestParam("tipo") Optional<Tipo> tipo,
            Pageable pageable, HttpServletRequest request) {
        Page<Vivienda> data = viviendaService.findByArgs(ciudad,precioMax,precioMin,provincia,tamanio,numHabs,tipo, pageable);

    if (data.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Page<GetViviendaDto> result = data.map(viviendaDtoConverter::viviendaToGetViviendaDto);

            UriComponentsBuilder uriBuilder =
                    UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());

            return ResponseEntity.ok().header("link", paginationLinksUtil.createLinkHeader(data, uriBuilder)).body(result);
        }
    }

    @Operation(summary = "Conseguir una vivienda con todos sus dedtalles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se encuentra la vivienda solicitada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado la vivienda con el id proporcionado.",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Usuario no autenticado",
                    content = @Content),
    })
    @GetMapping("{id}")
    public ResponseEntity<?> detalleVivienda (
            @Parameter(description = "ID de la Vivienda que desea buscar")
            @PathVariable Long id
    ){
        if (viviendaService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .ok()
                .body(viviendaService.encontrarViviendaPorId(id).map(viviendaDtoConverter::viviendaToGetViviendaDetailDto));
    }

    @Operation(summary = "Se crea un interesado con el interes a una vivienda que ya existe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
            description = "Se crea un interesado con interes",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Usuario.class))}),
            @ApiResponse(responseCode = "404",
            description = "No se encuentra la vivienda",
            content = @Content),
            @ApiResponse(responseCode = "400",
            description = "Ha introducido datos erroneos",
            content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content),
    })
    @PostMapping("/{id}/meinteresa")
    public ResponseEntity<GetInteresadoInteresaDto> createInteresado(@PathVariable("id") Long id, @RequestBody CreateInteresadoInteresaDto dto, @AuthenticationPrincipal Usuario usuario){

        if (viviendaService.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }else if(!usuario.getRol().equals(UserRole.PROPIETARIO) ){
            return new ResponseEntity<GetInteresadoInteresaDto>(HttpStatus.FORBIDDEN);
        }else {
            Optional<Vivienda> v = viviendaService.findById(id);
            Interesa interesa = Interesa.builder()
                    .mensaje(dto.getMensaje())
                    .build();
            interesa.addToUsuario(usuario);
            interesa.addToVivienda(v.get());
            interesaService.save(interesa);
            GetInteresadoInteresaDto iDto = propietarioDtoConverter.interesadoToGetInteresadoInteresaDto(usuario,interesa);
            return ResponseEntity.status(HttpStatus.CREATED).body(iDto);
        }
    }


    @Operation(summary = "Se a??ade una inmobiliaria existente a una vivienda existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se a??ade correctamente la inmobiliaria a la vivienda",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se encuentra la vivienda o la inmobiliaria",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Los datos introducidos son erroneos",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content),
    })
    @PostMapping("/{id1}/inmobiliaria/{id2}")
    public ResponseEntity<?> addViviendaAInmobiliaria ( @PathVariable Long id1, @PathVariable Long id2, @AuthenticationPrincipal Usuario usuario){
        Optional<Vivienda> optionalVivienda=viviendaService.findById(id1);
        Optional<Inmobiliaria> optionalInmobiliaria= inmobiliariaService.findById(id2);
        Usuario propietario = usuarioService.findPropietario(id1).get();
        if (optionalInmobiliaria.isEmpty() || optionalVivienda.isEmpty()){
            return ResponseEntity.notFound().build();
        }else if( usuario.getRol().equals(UserRole.ADMIN) ||
                (usuario.getRol().equals(propietario.getRol()) &&
                        usuario.getId().equals(propietario.getId()))){
            Vivienda vivienda = optionalVivienda.get();
            Inmobiliaria inmobiliaria = optionalInmobiliaria.get();
            //vivienda.setInmobiliaria(inmobiliaria);
            vivienda.addToInmobiliaria(inmobiliaria);
            viviendaService.save(vivienda);
            GetViviendaInmobiliariaDto viviendaDto = viviendaDtoConverter.viviendaToGetViviendaInmobiliariaDto(vivienda, inmobiliaria);
            return ResponseEntity.status(HttpStatus.CREATED).body(viviendaDto);
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @Operation(summary = "Se listan las viviendas con m??s interesados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se listan las viviendas con m??s interesados.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ninguna vivienda.",
                    content = @Content)
    })
    @GetMapping("/top")
    public ResponseEntity<List<?>> buscarViviendaParametros (
            @RequestParam("n")
            @PathVariable("limit")int limit, HttpServletRequest request) {

        List<Vivienda> data = viviendaService.findAll();

        if (data.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return buildResponseFromQuery(viviendaService.findAllByInteresas(limit),viviendaDtoConverter::viviendaToGetViviendaDto);

        }
    }

    private ResponseEntity<List<?>> buildResponseFromQuery(List<Vivienda> data, Function<Vivienda, GetViviendaDto> function) {
        if (data.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(data.stream()
                    //.map(dtoConverter::convertAlumnoToGetAlumnoDto)
                    .map(function)
                    .collect(Collectors.toList())
            );

    }

        @Operation(summary = "Se edita una vivienda existente.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201",
                        description = "Se ha editado correctamente la vivienda.",
                        content = {@Content(mediaType = "application/json",
                                schema = @Schema(implementation = Vivienda.class))}),
                @ApiResponse(responseCode = "404",
                        description = "No se encuentra la vivienda solicitada",
                        content = @Content),
                @ApiResponse(responseCode = "400",
                        description = "Los datos introducidos son erroneos",
                        content = @Content),
                @ApiResponse(responseCode = "403",
                        description = "Acceso denegado",
                        content = @Content),
        })
    @PutMapping("/{id}")
    public ResponseEntity<?> editVivienda (
    @Parameter(description = "ID de la Vivienda que desea buscar")
    @PathVariable Long id,
    @RequestBody CreateViviendaDto dto, @AuthenticationPrincipal Usuario usuario){
            Optional<Vivienda> viviendaOptional = viviendaService.findById(id);
             Usuario propietario = viviendaService.findById(id).get().getPropietario();
        if(viviendaOptional.isEmpty()){
            return ResponseEntity.notFound().build();

        } else if( usuario.getRol().equals(UserRole.ADMIN) ||
                    (usuario.getRol().equals(UserRole.PROPIETARIO) &&
                            usuario.getId().equals(propietario.getId()))){
            Vivienda vNueva;
            vNueva = viviendaDtoConverter.createViviendaDtoToVivienda(dto);
            viviendaService.edit(vNueva);
            return ResponseEntity.status(HttpStatus.CREATED).body(viviendaService.encontrarViviendaPorId(id).map(viviendaDtoConverter::viviendaToGetViviendaDetailDto));
        }
            else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    }

    @Operation(summary = "Elimina el inter??s de un interesado por una vivienda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha eliminado el inter??s con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado una vivienda con el id aportado.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content),
    })
    @DeleteMapping("/{id1}/meInteresa/")
    public ResponseEntity<?> deleteInteresDeInteresado(@PathVariable Long id1, @AuthenticationPrincipal Usuario usuario) {
            Optional<Vivienda> viviendaOptional = viviendaService.findById(id1);
        if (viviendaService.findById(id1).isEmpty() || usuarioService.findById(usuario.getId()).isEmpty() || interesaService.findByInteresaPk(id1,usuario.getId())==null) { //el m??todo findByInteresaService deber??a no puede acceder al m??todo isEmpty().
            return ResponseEntity.notFound().build();

        } else if (usuario.getRol().equals(UserRole.PROPIETARIO) && usuario.getId().equals(viviendaOptional.get().getPropietario().getId()) ) {
            interesaService.eliminarInteresaPorPk(id1,usuario.getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


