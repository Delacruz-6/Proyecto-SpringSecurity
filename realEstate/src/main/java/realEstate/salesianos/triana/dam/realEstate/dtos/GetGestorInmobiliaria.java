package realEstate.salesianos.triana.dam.realEstate.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetGestorInmobiliaria {
    private Long id;
    private String nombre, apellidos, dirrecion, email,avatar;
    private Integer telefono;


}
