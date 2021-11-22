package realEstate.salesianos.triana.dam.realEstate.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CreateInteresadoInteresaVivienda {
    private Long interesado_id;
    private Long vivienda_id;
    private String mensaje;
}
