package realEstate.salesianos.triana.dam.realEstate.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPropietarioDto {

    private Long id;
    private String nombre;
    private String apellidos;
    private String avatar;
    private int numViviendas;


}
