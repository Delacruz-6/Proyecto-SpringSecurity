package realEstate.salesianos.triana.dam.realEstate.dtos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import realEstate.salesianos.triana.dam.realEstate.models.*;
import realEstate.salesianos.triana.dam.realEstate.services.InmobiliariaService;
import realEstate.salesianos.triana.dam.realEstate.services.PropietarioService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ViviendaDtoConverter {
    private final InteresaDtoConverter interesaDtoConverter = new InteresaDtoConverter();
    private final PropietarioDtoConverter propietarioDtoConverter = new PropietarioDtoConverter();
    private final InmobiliariaDtoConverter inmobiliariaDtoConverter = new InmobiliariaDtoConverter();
   @Autowired
    private  InmobiliariaService inmobiliariaService ;
    @Autowired
    private PropietarioService propietarioService ;

    public GetViviendaDto viviendaToGetViviendaDto (Vivienda v){

        GetViviendaDto result = new GetViviendaDto();
        result.setCiudad(v.getPoblacion());
        result.setDireccion(v.getDireccion());
        result.setPrecio(v.getPrecio());
        result.setProvincia(v.getProvincia());
        result.setId(v.getId());
        result.setAvatar(v.getAvatar());
        result.setMetrosCuadrados(v.getMetrosCuadrados());
        result.setNumHabitaciones(v.getNumHabitaciones());
        result.setTipo(v.getTipo().toString());
        result.setTitulo(v.getTitulo());
        result.setMeInteresas(v.getIntereses().size());
        return result;
    }


    public GetViviendaDetailDto viviendaToGetViviendaDetailDto (Vivienda v) {
        List<Interesa> listaInteresados = new ArrayList<>();
        List<GetInteresaDto> interesaDtos = new ArrayList<>();
        GetViviendaDetailDto result = new GetViviendaDetailDto();

        result.setId(v.getId());
        result.setTitulo(v.getTitulo());
        result.setDescripcion(v.getDescripcion());
        result.setAvatar(v.getAvatar());
        result.setLatitudLongitud(v.getLatitudLongitud());
        result.setDireccion(v.getDireccion());
        result.setCodigoPostal(v.getCodigoPostal());
        result.setCiudad(v.getPoblacion());
        result.setProvincia(v.getProvincia());
        result.setTipo(v.getTipo().toString());
        result.setPrecio(v.getPrecio());
        result.setNumHabitaciones(v.getNumHabitaciones());
        result.setMetrosCuadrados(v.getMetrosCuadrados());
        result.setNumBanos(v.getNumBanos());
        result.setTienePiscina(v.isTienePiscina());
        result.setTieneAscensor(v.isTieneAscensor());
        result.setTieneGaraje(v.isTieneGaraje());
        result.setMeInteresas(v.getIntereses().size());

        if (v.getInmobiliaria() == null) {
            result.setInmobiliaria(null);
        } else {

            result.setInmobiliaria(inmobiliariaDtoConverter.inmobiliariaToGetInmobiliariaDetailDto(v.getInmobiliaria()));

        }

        if (v.getPropietario() == null) {
            result.setPropietario(null);
        } else {
            result.setPropietario(propietarioDtoConverter.convertPropietarioToGetPropietarioDetailsDto(v.getPropietario()));

        }

        for (int i = 0; i < v.getIntereses().size(); i++) {
            listaInteresados.add(v.getIntereses().get(i));
        }

        interesaDtos = v.getIntereses().stream().map(interesaDtoConverter::interesaToGetInteresaDto).collect(Collectors.toList());

        result.setInteresas(interesaDtos);
        return result;
    }

    public GetViviendaInmobiliariaDto viviendaToGetViviendaInmobiliariaDto(Vivienda v, Inmobiliaria i){

        GetViviendaInmobiliariaDto result = new GetViviendaInmobiliariaDto();
        result.setCiudad(v.getPoblacion());
        result.setDireccion(v.getDireccion());
        result.setProvincia(v.getProvincia());
        result.setPrecio(v.getPrecio());
        result.setId(v.getId());
        result.setAvatar(v.getAvatar());
        result.setMetrosCuadrados(v.getMetrosCuadrados());
        result.setNumHabitaciones(v.getNumHabitaciones());
        result.setTipo(v.getTipo().toString());
        result.setTitulo(v.getTitulo());
        result.setMeInteresas(v.getIntereses().size());
        result.setIdInmobiliaria(i.getId());
        result.setNombreInmobiliaria(i.getNombre());

        return result;
    }

    public Vivienda createViviendaDtoToVivienda  (CreateViviendaDto v) {

        Vivienda result = new Vivienda();
        Tipo t = Tipo.valueOf(v.getTipo());

        result.setId(v.getId());
        result.setTitulo(v.getTitulo());
        result.setDescripcion(v.getDescripcion());
        result.setAvatar(v.getAvatar());
        result.setLatitudLongitud(v.getLatitudLongitud());
        result.setDireccion(v.getDireccion());
        result.setCodigoPostal(v.getCodigoPostal());
        result.setPoblacion(v.getCiudad());
        result.setProvincia(v.getProvincia());
        result.setTipo(t);
        result.setPrecio(v.getPrecio());
        result.setNumHabitaciones(v.getNumHabitaciones());
        result.setMetrosCuadrados(v.getMetrosCuadrados());
        result.setNumBanos(v.getNumBanos());
        result.setTienePiscina(v.isTienePiscina());
        result.setTieneAscensor(v.isTieneAscensor());
        result.setTieneGaraje(v.isTieneGaraje());
        result.setInmobiliaria(inmobiliariaService.findById(v.getInmobiliaria().getId()).get());
        result.setPropietario(propietarioService.findById(v.getPropietario().getId()).get());

        return result;
    }
}
