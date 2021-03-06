package realEstate.salesianos.triana.dam.realEstate.dtos;

import org.springframework.stereotype.Component;
import realEstate.salesianos.triana.dam.realEstate.models.Inmobiliaria;
import realEstate.salesianos.triana.dam.realEstate.models.Interesa;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;

import java.util.ArrayList;
import java.util.List;

@Component
public class InmobiliariaDtoConverter {

    public Inmobiliaria createInmobiliariaDtoToInmobiliaria(GetInmobiliariaDto c){
        return new Inmobiliaria(
                c.getNombre(),
                c.getAvatar()
        );
    }
    public GetInmobiliariaGestorPostDto inmobiliariaToGetInmobiliariaGestorDtoPost(Usuario u, Inmobiliaria i){

        List<String> nombresGestores = new ArrayList<>();
        for (int j = 0; j < i.getGestores().size(); j++){
            nombresGestores.add(i.getGestores().get(j).getNombre());
        }

        return GetInmobiliariaGestorPostDto.builder()
                .id(i.getId())
                .nombre(i.getNombre())
                .avatar(i.getAvatar())
                .idGestor(u.getId())
                .build();
    }

    public GetInmobiliariaGestorDetailDto inmobiliariaToGetInmobiliariaViviendasDtoDetail(Inmobiliaria i){

        List<String> nombresGestores = new ArrayList<>();
        for (int j = 0; j < i.getGestores().size(); j++){
            nombresGestores.add(i.getGestores().get(j).getNombre());
        }
        return GetInmobiliariaGestorDetailDto.builder()
                .id(i.getId())
                .nombre(i.getNombre())
                .avatar(i.getAvatar())
                .numViviendas(i.getViviendas().size())
                .nombresGestores(nombresGestores)
                .build();
    }

    public GetInmobiliariaDto inmobiliariaToGetInmobiliariaDto(Inmobiliaria i){
        int numViviendas = i.getViviendas().size();
        GetInmobiliariaDto result = new GetInmobiliariaDto();
        result.setId(i.getId());
        result.setNombre(i.getNombre());
        result.setAvatar(i.getAvatar());
        result.setNumViviendas(numViviendas);
        return result;

    }
    public GetGestorInmobiliaria gestoresToGetGestorDto(Usuario u){

        return GetGestorInmobiliaria.builder()
                .id(u.getId())
                .nombre(u.getNombre())
                .apellidos(u.getApellidos())
                .dirrecion(u.getDireccion())
                .avatar(u.getAvatar())
                .telefono(u.getTelefono())
                .build();

    }

    public GetInmobiliariaViviendasDto inmobiliariaToGetInmobiliariaViviendasDto(Inmobiliaria i){
        List<String> nombres = new ArrayList<>();
        for (int j = 0; j < i.getViviendas().size(); j++){
            nombres.add(i.getViviendas().get(j).getTitulo());
        }
        List<String> nombresGestores = new ArrayList<>();
        for (int j = 0; j < i.getViviendas().size(); j++){
            nombres.add(i.getGestores().get(j).getNombre());
        }
        return GetInmobiliariaViviendasDto.builder()
                .id(i.getId())
                .nombre(i.getNombre())
                .avatar(i.getAvatar())
                .numViviendas(i.getViviendas().size())
                .nombreVivienda(nombres)
                .nombreGestor(nombresGestores)
                .build();
    }

    public GetInmobiliariaDetailDto inmobiliariaToGetInmobiliariaDetailDto(Inmobiliaria i){
        GetInmobiliariaDetailDto result = new GetInmobiliariaDetailDto();
        result.setId(i.getId());
        result.setNombre(i.getNombre());
        result.setEmail(i.getEmail());
        result.setAvatar(i.getAvatar());
        result.setTelefono(i.getTelefono());
        return result;
    }
}
