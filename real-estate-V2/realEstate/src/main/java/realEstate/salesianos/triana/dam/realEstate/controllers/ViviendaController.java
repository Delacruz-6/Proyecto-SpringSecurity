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
import realEstate.salesianos.triana.dam.realEstate.util.PaginationLinksUtil;


import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
      private final InteresadoService interesadoService;
      private final InteresaService interesaService;
      private final InteresadoDtoConverter interesadoDtoConverter;
      private final PropietarioService propietarioService;

    @Operation(summary = "Se crea una vivienda y si el propietario no existe, también lo crea")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se crea una vivienda con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "400",
                    description = "La estructura de la petición estaba mal formulada",
                    content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<Vivienda> createVivienda(@RequestBody Vivienda vivienda) {
        if (!propietarioService.findById(vivienda.getPropietario().getId()).isPresent()) {
            propietarioService.save(vivienda.getPropietario());
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(viviendaService.save(vivienda));
    }

    @Operation(summary = "Eliminación de una vivienda por su id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Borrar vivienda con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado una vivienda con ese id.",
                    content = @Content),})
    @DeleteMapping("/{id}")
    @CrossOrigin
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if(viviendaService.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            viviendaService.findById(id).get().removeViviendasToIntereses();
            viviendaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "Eliminación de la inmobiliaria asociada a la vivienda según su id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Borrado de la inmobiliaria con éxito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado una inmobiliaria en esta vivienda con ese id.",
                    content = @Content),
    })
    @DeleteMapping("/{id1}/inmobiliaria/")
    @CrossOrigin
    public ResponseEntity<?> deleteInmobiliariaToVivienda(@PathVariable Long id1) {

        Optional<Vivienda> viviendaOptional=viviendaService.findById(id1);

        if (viviendaOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            Vivienda vivienda = viviendaOptional.get();
            Inmobiliaria inmobiliaria = new Inmobiliaria();
            vivienda.setInmobiliaria(inmobiliaria);
            vivienda.removeFromInmobiliaria(inmobiliaria);
            viviendaService.save(vivienda);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

@Operation(summary = "Listar todas las viviendas existentes con paginación y filtrado")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "Se listan todas las viviendas",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = Vivienda.class))}),
        @ApiResponse(responseCode = "404",
                description = "No se ha encontrado ninguna vivienda",
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

            return ResponseEntity.ok().header("link", paginationLinksUtil.createLinkHeader(result, uriBuilder)).body(result);
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
            schema = @Schema(implementation = Interesado.class))}),
            @ApiResponse(responseCode = "404",
            description = "No se encuentra la vivienda",
            content = @Content),
            @ApiResponse(responseCode = "400",
            description = "Ha introducido datos erroneos",
            content = @Content)
    })
    @PostMapping("/{id}/meinteresa")
    public ResponseEntity<GetInteresadoInteresaDto> createInteresado(@PathVariable("id") Long id, @RequestBody CreateInteresadoInteresaDto dto){


        if (viviendaService.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            Optional<Vivienda> v = viviendaService.findById(id);
            Interesado interesado = interesadoDtoConverter.createInteresadoDtoToInteresado(dto);
            Interesa interesa = Interesa.builder()
                    .mensaje(dto.getMensaje())
                    .build();
            interesa.addToInteresado(interesado);
            interesa.addToVivienda(v.get());
            interesadoService.save(interesado);
            interesaService.save(interesa);
            GetInteresadoInteresaDto iDto = interesadoDtoConverter.interesadoToGetInteresadoInteresaDto(interesado,interesa);
            return ResponseEntity.status(HttpStatus.CREATED).body(iDto);
        }
    }

    @Operation(summary = "Se añade a una vivienda existente un interesado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
            description = "Se añade correctamente el interesado a la vivienda",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Interesado.class))}),
            @ApiResponse(responseCode = "400",
            description = "Ha introducido datos erroneos",
            content = @Content),
            @ApiResponse(responseCode = "404",
            description = "No se encuentra la vivienda o el interesado",
            content = @Content),
    })
    @PostMapping("/{id}/meinteresa/{id2}")
    public ResponseEntity<GetInteresadoInteresaVivienda> createInteresadoExistente(@PathVariable Long id, @PathVariable Long id2,
                                                                                   @RequestBody CreateInteresadoInteresaVivienda g){

        Optional<Vivienda> vivOp = viviendaService.findById(id);
        Optional<Interesado> inteOp = interesadoService.findById(id2);
        if (viviendaService.findById(id).isEmpty() || interesadoService.findById(id2).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else{
            Vivienda v = vivOp.get();
            Interesado i = inteOp.get();
            Interesa interesa = Interesa.builder()
                    .mensaje(g.getMensaje())
                    .build();
            interesa.addToInteresado(i);
            interesa.addToVivienda(v);
            interesadoService.save(i);
            interesaService.save(interesa);
            GetInteresadoInteresaVivienda interesadoInteresaVivienda = interesadoDtoConverter.interesadoToGetInteresadoInteresaVivienda(interesa);
            return ResponseEntity.status(HttpStatus.CREATED).body(interesadoInteresaVivienda);
        }


    }

    @Operation(summary = "Se añade una inmobiliaria existente a una vivienda existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se añade correctamente la inmobiliaria a la vivienda",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se encuentra la vivienda o la inmobiliaria",
                    content = @Content),
            @ApiResponse(responseCode = "400",
                    description = "Los datos introducidos son erroneos",
                    content = @Content)
    })
    @PostMapping("/{id1}/inmobiliaria/{id2}")
    public ResponseEntity<?> addViviendaAInmobiliaria ( @PathVariable Long id1, @PathVariable Long id2){
        Optional<Vivienda> optionalVivienda=viviendaService.findById(id1);
        Optional<Inmobiliaria> optionalInmobiliaria= inmobiliariaService.findById(id2);
        if (optionalInmobiliaria.isEmpty() || optionalVivienda.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{

            Vivienda vivienda = optionalVivienda.get();
            Inmobiliaria inmobiliaria = optionalInmobiliaria.get();
            //vivienda.setInmobiliaria(inmobiliaria);
            vivienda.addToInmobiliaria(inmobiliaria);
            viviendaService.save(vivienda);
            GetViviendaInmobiliariaDto viviendaDto = viviendaDtoConverter.viviendaToGetViviendaInmobiliariaDto(vivienda, inmobiliaria);
            return ResponseEntity.status(HttpStatus.CREATED).body(viviendaDto);
        }

    }

    @Operation(summary = "Se listan las viviendas con más interesados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se listan las viviendas con más interesados.",
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
                        content = @Content)
        })
    @PutMapping("/{id}")
    public ResponseEntity<?> editVivienda (
    @Parameter(description = "ID de la Vivienda que desea buscar")
    @PathVariable Long id,
    @RequestBody CreateViviendaDto dto){

        if(viviendaService.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();

    }else{
            Vivienda vNueva = new Vivienda();
            vNueva = viviendaDtoConverter.createViviendaDtoToVivienda(dto);
           viviendaService.edit(vNueva);
            return ResponseEntity.status(HttpStatus.CREATED).body(viviendaService.encontrarViviendaPorId(id).map(viviendaDtoConverter::viviendaToGetViviendaDetailDto));
    
    }

    }

    @Operation(summary = "Elimina el interés de un interesado por una vivienda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha eliminado el interés con exito",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vivienda.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado una vivienda con el id aportado.",
                    content = @Content),
    })
    @DeleteMapping("/{id1}/meInteresa/{id2}")
    @CrossOrigin
    public ResponseEntity<?> deleteInteresDeInteresado(@PathVariable Long id1,@PathVariable Long id2) {

        if (viviendaService.findById(id1).isEmpty() || interesadoService.findById(id2).isEmpty() || interesaService.findByInteresaPk(id1,id2)==null) { //el método findByInteresaService debería no puede acceder al método isEmpty().
            return ResponseEntity.notFound().build();

        } else {
            interesaService.eliminarInteresaPorPk(id1,id2);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

}
