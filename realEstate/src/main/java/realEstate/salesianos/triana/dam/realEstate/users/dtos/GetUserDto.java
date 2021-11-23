package realEstate.salesianos.triana.dam.realEstate.users.dtos;

import lombok.*;
import realEstate.salesianos.triana.dam.realEstate.users.models.UserRole;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUserDto {

    private String email;
    private String rol;
    private String userName;
    private String direccion;
    private Integer telefono;
    private String avatar;


}
