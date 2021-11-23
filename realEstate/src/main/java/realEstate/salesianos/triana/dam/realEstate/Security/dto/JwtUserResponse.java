package realEstate.salesianos.triana.dam.realEstate.Security.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtUserResponse {
    private String email;
    private String fullName;
    private String avatar;
    private String rol;
    private String token;
    private String direccion;
    private Integer telefono;

}
