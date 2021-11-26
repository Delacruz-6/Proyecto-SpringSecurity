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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import realEstate.salesianos.triana.dam.realEstate.Security.dto.JwtUserResponse;
import realEstate.salesianos.triana.dam.realEstate.Security.jwt.JwtAuthorizationFilter;
import realEstate.salesianos.triana.dam.realEstate.Security.jwt.JwtTokenProvider;
import realEstate.salesianos.triana.dam.realEstate.dtos.GetPropietarioConViviendasDto;
import realEstate.salesianos.triana.dam.realEstate.dtos.GetPropietarioDto;
import realEstate.salesianos.triana.dam.realEstate.dtos.PropietarioDtoConverter;
import realEstate.salesianos.triana.dam.realEstate.users.models.UserRole;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;
import realEstate.salesianos.triana.dam.realEstate.users.repositories.UsuarioRepository;
import realEstate.salesianos.triana.dam.realEstate.users.services.UsuarioService;
import realEstate.salesianos.triana.dam.realEstate.util.PaginationLinksUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/propietario")
@RequiredArgsConstructor
public class PropietarioController {

    private final UsuarioService propietarioService;
    private final PropietarioDtoConverter dtoConverter;
    private final UsuarioRepository repository;
    private final PaginationLinksUtil paginationLinksUtil;




    @Operation(summary = "Listar todos los propietarios existentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se listan todos los propietarios",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningun propietario.",
                    content = @Content),
    })
    @GetMapping("/")
    public ResponseEntity<?> findAll(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            HttpServletRequest request) {

        Page<Usuario> data = propietarioService.loadUserByRol(UserRole.PROPIETARIO,pageable);

        if(data.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            Page<GetPropietarioDto> result = data.map(dtoConverter::propietarioToGetPropietarioDto);

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
            return ResponseEntity.ok().header("link", paginationLinksUtil.createLinkHeader(result,uriBuilder)).body(result);
        }
    }

    @Operation(summary = "Obtenemos todos los datos de un propietario con algunos datos de sus viviendas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se encuentra el propietario con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el propietario.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<?>> findOnePropietario(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        Optional<Usuario> propietario = propietarioService.loadUserById2(id);

        if (propietario.isEmpty()){
            return ResponseEntity.notFound().build();
        } else if( usuario.getRol().equals(UserRole.ADMIN) ||
                (propietario.get().getRol().equals(usuario.getRol()) &&  propietario.get().getId().equals(usuario.getId()))){
            List<GetPropietarioConViviendasDto> propietarioDto = propietario.stream()
                    .map(dtoConverter::convertPropietarioToGetPropietarioConViviendasDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(propietarioDto);
        }else {
            return new ResponseEntity<List<?>>(HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "Eliminamos un propietario por su id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Borrado del propietario con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado un propietario con ese id.",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Acceso denegado",
                    content = @Content),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id ,@AuthenticationPrincipal Usuario usuario) {

        Optional<Usuario> propietarioOptional = propietarioService.loadUserById2(id);

        if (propietarioOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        } else if (usuario.getRol().equals(UserRole.ADMIN) ||
                (propietarioOptional.get().getRol().equals(usuario.getRol()) && propietarioOptional.get().getId().equals(usuario.getId()))) {
            return ResponseEntity.notFound().build();
        } else{
            Usuario propietario = propietarioOptional.get();
            propietario.nullearPropietarioDeViviendas();
            repository.deleteById(id);
        }
        return new ResponseEntity<List<?>>(HttpStatus.FORBIDDEN);
    }
}
