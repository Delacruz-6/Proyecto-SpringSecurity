package realEstate.salesianos.triana.dam.realEstate.users.dtos.gestor;


import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class CreatedGestorDto {

    private String username;
    private String avatar;
    private String nombre;
    private String apellidos;
    private String direccion;
    private Integer telefono;
    private String email;
    private String password;
    private String password2;
    private Long idInmobiliaria;
}
