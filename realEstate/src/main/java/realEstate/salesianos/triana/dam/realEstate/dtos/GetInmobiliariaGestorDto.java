package realEstate.salesianos.triana.dam.realEstate.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetInmobiliariaGestorDto {
    private Long id;
    private String nombre;
    private String avatar;

}
