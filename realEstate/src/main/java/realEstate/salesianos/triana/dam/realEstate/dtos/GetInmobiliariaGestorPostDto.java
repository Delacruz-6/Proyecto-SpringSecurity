package realEstate.salesianos.triana.dam.realEstate.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetInmobiliariaGestorPostDto {
    private Long id;
    private String nombre;
    private String avatar;
    private Long idGestor;

}
