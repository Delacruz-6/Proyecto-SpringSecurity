package realEstate.salesianos.triana.dam.realEstate.users.dtos;

import org.springframework.stereotype.Component;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;

@Component
public class UserDtoConverter {

    public GetUserDto convertUserEntityToGetUserDto(Usuario user) {
        return GetUserDto.builder()

                .userName(user.getNombre())
                .email(user.getEmail())
                .direccion(user.getDireccion())
                .telefono(user.getTelefono())
                .rol(user.getRol().name())
                .build();
    }
}
