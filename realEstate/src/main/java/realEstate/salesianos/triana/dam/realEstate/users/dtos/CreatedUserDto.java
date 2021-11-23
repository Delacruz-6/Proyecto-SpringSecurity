package realEstate.salesianos.triana.dam.realEstate.users.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatedUserDto {

    private String username;
    private String avatar;
    private String nombre;
    private String apellidos;
    private String direccion;
    private Integer telefono;
    private String email;
    private String password;
    private String password2;

}
