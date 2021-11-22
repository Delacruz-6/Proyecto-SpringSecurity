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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import realEstate.salesianos.triana.dam.realEstate.dtos.GetInmobiliariaDto;
import realEstate.salesianos.triana.dam.realEstate.dtos.InmobiliariaDtoConverter;
import realEstate.salesianos.triana.dam.realEstate.models.Inmobiliaria;
import realEstate.salesianos.triana.dam.realEstate.services.InmobiliariaService;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<List<GetInmobiliariaDto>> findOne(@PathVariable Long id){

        Optional<Inmobiliaria> inmo = inmobiliariaService.findById(id);
        if (inmobiliariaService.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else{
            List<GetInmobiliariaDto> inmobiliariaDto = inmo.stream()
                    .map(inmobiliariaDtoConverter::inmobiliariaToGetInmobiliariaViviendasDto)
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
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if (inmobiliariaService.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else{
            Optional<Inmobiliaria> inmo = inmobiliariaService.findById(id);
            Inmobiliaria inmo1 = inmo.get();

            if (inmo.isPresent()){
                inmo1.nullearInmobiliariaDeViviendas();
                inmobiliariaService.deleteById(id);

            }
            return ResponseEntity.noContent().build();
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
    })
    @PostMapping("/")
    public ResponseEntity<Inmobiliaria> create(@RequestBody Inmobiliaria inmobiliaria) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(inmobiliariaService.save(inmobiliaria));
    }

}
