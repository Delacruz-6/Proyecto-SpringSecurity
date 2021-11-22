package realEstate.salesianos.triana.dam.realEstate.dtos;

import org.springframework.stereotype.Component;
import realEstate.salesianos.triana.dam.realEstate.models.Interesa;
import realEstate.salesianos.triana.dam.realEstate.models.Interesado;
@Component
public class InteresadoDtoConverter {
    public Interesado createInteresadoDtoToInteresado(CreateInteresadoInteresaDto c){
        return new Interesado(
                c.getNombre(),
                c.getApellidos(),
                c.getDireccion(),
                c.getEmail(),
                c.getTelefono(),
                c.getAvatar()
        );
    }

    public GetInteresadoDto interesadoToGetInteresadoDto(Interesado i){
        GetInteresadoDto result = new GetInteresadoDto();
        result.setId(i.getId());
        result.setNombre(i.getNombre());
        result.setApellidos(i.getApellidos());
        result.setDireccion(i.getDireccion());
        result.setEmail(i.getEmail());
        result.setTelefono(i.getTelefono());
        result.setAvatar(i.getAvatar());
        return result;
    }

    public GetInteresadoDto2 interesadoToGetInteresadoDto2(Interesado i){
        GetInteresadoDto2 result = new GetInteresadoDto2();
        result.setId(i.getId());
        result.setNombre(i.getNombre());
        result.setApellidos(i.getApellidos());
        result.setAvatar(i.getAvatar());
        return result;
    }
    public GetInteresadoInteresaDto interesadoToGetInteresadoInteresaDto(Interesado i, Interesa in){
        return GetInteresadoInteresaDto.builder()
                .nombre(i.getNombre())
                .apellidos(i.getApellidos())
                .direccion(i.getDireccion())
                .telefono(i.getTelefono())
                .email(i.getEmail())
                .avatar(i.getAvatar())
                .mensaje(in.getMensaje())
                .build();
    }
    public GetInteresadoInteresaVivienda interesadoToGetInteresadoInteresaVivienda(Interesa in){
        return GetInteresadoInteresaVivienda.builder()
                .mensaje(in.getMensaje())
                .build();
    }
}

