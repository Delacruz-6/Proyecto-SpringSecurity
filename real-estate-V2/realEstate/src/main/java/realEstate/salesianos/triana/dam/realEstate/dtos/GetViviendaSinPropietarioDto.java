package realEstate.salesianos.triana.dam.realEstate.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetViviendaSinPropietarioDto {

    private Long id;
    private String titulo;
    private String avatar;
    private String poblacion;
    private double precio;
}
