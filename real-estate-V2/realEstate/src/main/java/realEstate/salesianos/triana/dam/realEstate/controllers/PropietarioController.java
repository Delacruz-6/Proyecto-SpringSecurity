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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import realEstate.salesianos.triana.dam.realEstate.dtos.GetPropietarioConViviendasDto;
import realEstate.salesianos.triana.dam.realEstate.dtos.GetPropietarioDetailsDto;
import realEstate.salesianos.triana.dam.realEstate.dtos.GetPropietarioDto;
import realEstate.salesianos.triana.dam.realEstate.dtos.PropietarioDtoConverter;
import realEstate.salesianos.triana.dam.realEstate.models.Propietario;
import realEstate.salesianos.triana.dam.realEstate.repositories.PropietarioRepository;
import realEstate.salesianos.triana.dam.realEstate.services.PropietarioService;
import realEstate.salesianos.triana.dam.realEstate.util.PaginationLinksUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/propietario")
@RequiredArgsConstructor
public class PropietarioController {

    private final PropietarioService propietarioService;
    private final PropietarioDtoConverter dtoConverter;
    private final PropietarioRepository repository;
    private final PaginationLinksUtil paginationLinksUtil;


    @Operation(summary = "Listar todos los propietarios existentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se listan todos los propietarios",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Propietario.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado ningun propietario.",
                    content = @Content),
    })
    @GetMapping("/")
    public ResponseEntity<?> findAll(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            HttpServletRequest request) {

        Page<Propietario> data = propietarioService.findAll(pageable);

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
                            schema = @Schema(implementation = Propietario.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado el propietario.",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<GetPropietarioConViviendasDto> findOne(@PathVariable Long id) {
        return ResponseEntity.of(propietarioService.findById(id).map(dtoConverter::convertPropietarioToGetPropietarioConViviendasDto));
    }

    @Operation(summary = "Eliminamos un propietario por su id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Borrado del propietario con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Propietario.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado un propietario con ese id.",
                    content = @Content),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Propietario> propietario = propietarioService.findById(id);
        Propietario p1 = propietario.get();

        if (!propietario.isEmpty()) {
            p1.nullearPropietarioDeViviendas();
            repository.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }
}
