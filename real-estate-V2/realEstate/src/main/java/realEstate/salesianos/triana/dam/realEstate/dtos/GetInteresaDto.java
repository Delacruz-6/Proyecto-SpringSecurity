package realEstate.salesianos.triana.dam.realEstate.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetInteresaDto {
    private Long interesado_id;
    private Long vivienda_id;
    private Date createdDate;
    private String mensaje;
    private GetInteresadoDto interesadoDto;
}
