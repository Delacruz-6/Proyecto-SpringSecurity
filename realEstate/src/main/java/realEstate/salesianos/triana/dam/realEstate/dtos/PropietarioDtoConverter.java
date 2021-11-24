package realEstate.salesianos.triana.dam.realEstate.dtos;

import org.springframework.stereotype.Component;
import realEstate.salesianos.triana.dam.realEstate.models.Interesa;
import realEstate.salesianos.triana.dam.realEstate.models.Vivienda;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;

import java.util.stream.Collectors;

@Component
public class PropietarioDtoConverter {

    public Usuario createPropietarioDtoToPropietario(GetPropietarioDto c){
        return new Usuario(
                c.getNombre(),
                c.getApellidos()
        );
    }

    public GetPropietarioDto propietarioToGetPropietarioDto(Usuario p){
        int numViviendas = p.getViviendas().size();
        GetPropietarioDto result = new GetPropietarioDto();
        result.setId(p.getId());
        result.setNombre(p.getNombre());
        result.setApellidos(p.getApellidos());
        result.setAvatar(p.getAvatar());
        result.setNumViviendas(numViviendas);
        return result;
    }

    public GetPropietarioConViviendasDto convertPropietarioToGetPropietarioConViviendasDto(Usuario p){
        GetPropietarioConViviendasDto dto= new GetPropietarioConViviendasDto(
                p.getId(), p.getNombre(),
                p.getApellidos(), p.getDireccion(),
                p.getEmail(), p.getTelefono(),
                p.getAvatar(),
                p.getViviendas().stream().map(this::convertViviendaToGetViviendaSinPropietarioDto).collect(Collectors.toList()));
    return dto;

    }

    public GetViviendaSinPropietarioDto convertViviendaToGetViviendaSinPropietarioDto(Vivienda v){
        return GetViviendaSinPropietarioDto.builder()
                .id(v.getId())
                .titulo(v.getTitulo())
                .avatar(v.getAvatar())
                .poblacion(v.getPoblacion())
                .precio(v.getPrecio())
                .build();

    }

    public GetPropietarioDetailsDto convertPropietarioToGetPropietarioDetailsDto (Usuario p){
        return GetPropietarioDetailsDto.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .apellidos(p.getApellidos())
                .direccion(p.getDireccion())
                .email(p.getEmail())
                .telefono(p.getTelefono())
                .avatar(p.getAvatar())
                .build();
    }

    public GetPropietarioInteresaVivienda interesadoToGetInteresadoInteresaVivienda(Interesa in){
        return GetPropietarioInteresaVivienda.builder()
                .mensaje(in.getMensaje())
                .build();
    }
}
