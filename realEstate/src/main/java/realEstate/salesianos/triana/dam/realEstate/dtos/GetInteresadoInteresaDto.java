package realEstate.salesianos.triana.dam.realEstate.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class GetInteresadoInteresaDto{

    private String nombre,apellidos,direccion,email,avatar;
    private Integer telefono;
    private String mensaje;




}
