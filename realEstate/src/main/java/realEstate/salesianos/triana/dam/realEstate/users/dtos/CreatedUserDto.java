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
    private String fullname;
    private String email;
    private String password;
    private String password2;

}
