package realEstate.salesianos.triana.dam.realEstate.users.dtos.gestor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetGestorDto {

    private String email;
    private String rol;
    private String userName;
    private String direccion;
    private Integer telefono;
    //private Long idInmobiliaria;
}
