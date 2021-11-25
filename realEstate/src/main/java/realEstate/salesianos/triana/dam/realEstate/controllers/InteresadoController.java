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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import realEstate.salesianos.triana.dam.realEstate.dtos.GetInteresadoDto;
import realEstate.salesianos.triana.dam.realEstate.dtos.GetInteresadoDto2;
import realEstate.salesianos.triana.dam.realEstate.dtos.InteresadoDtoConverter;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;
import realEstate.salesianos.triana.dam.realEstate.users.services.UsuarioService;
import realEstate.salesianos.triana.dam.realEstate.util.PaginationLinksUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/interesado")
public class InteresadoController {

    private final PaginationLinksUtil paginationLinksUtil;
    //private final InteresadoService iService;
    private final UsuarioService usuarioService;
    private final InteresadoDtoConverter interesadoDtoConverter;

    @Operation(summary = "Listar todos los interesados existentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se listan todos los interesados",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningun interesado.",
                    content = @Content),
    })

    @GetMapping("/")
    public ResponseEntity<?> findAll(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                     HttpServletRequest request) {
        Page<Usuario> data = usuarioService.findAll(pageable);

        if(data.isEmpty()){
            return ResponseEntity.notFound().build();
        }else {
            Page<GetInteresadoDto2> result = data
                    .map(interesadoDtoConverter::interesadoToGetInteresadoDto2);
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
            return ResponseEntity.ok().header("link", paginationLinksUtil.createLinkHeader(result, uriBuilder)).body(result);
        }
    }

    @Operation(summary = "Ver detalle de un interesado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ven todos los atributos de un interesado con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningun interesado con ese identificador",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<GetInteresadoDto>> findOne (@PathVariable("id") Long id, @AuthenticationPrincipal Usuario usuario){

        Optional<Usuario> interesadoOptional = usuarioService.findById(id);

        if(interesadoOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else if(interesadoOptional.get().getRol().equals(usuario.getRol()) &&
                interesadoOptional.get().getId().equals(usuario.getId())) {
            List<GetInteresadoDto> interesadoDto = interesadoOptional
                    .stream().map(interesadoDtoConverter::interesadoToGetInteresadoDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(interesadoDto);
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }








}
