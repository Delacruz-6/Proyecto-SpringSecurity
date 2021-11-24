package realEstate.salesianos.triana.dam.realEstate.users.dtos.usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import realEstate.salesianos.triana.dam.realEstate.services.InmobiliariaService;
import realEstate.salesianos.triana.dam.realEstate.users.dtos.gestor.GetGestorDto;
import realEstate.salesianos.triana.dam.realEstate.users.dtos.usuario.GetUserDto;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;

@Component
@RequiredArgsConstructor
public class UserDtoConverter {

    private final InmobiliariaService inmobiliariaService;

    public GetUserDto convertUsuarioToGetUserDto(Usuario user) {
        return GetUserDto.builder()
                .userName(user.getNombre())
                .email(user.getEmail())
                .direccion(user.getDireccion())
                .telefono(user.getTelefono())
                .rol(user.getRol().name())
                .build();
    }

    public GetGestorDto convertGestorToGetUserDto(Usuario user) {
        return GetGestorDto.builder()
                .userName(user.getNombre())
                .email(user.getEmail())
                .direccion(user.getDireccion())
                .telefono(user.getTelefono())
                .rol(user.getRol().name())
                //.idInmobiliaria(user.getInmobiliaria().getId())
                .build();
    }
}
