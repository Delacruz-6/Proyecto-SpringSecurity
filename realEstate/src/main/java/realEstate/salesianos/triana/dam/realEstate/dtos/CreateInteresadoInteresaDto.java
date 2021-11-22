package realEstate.salesianos.triana.dam.realEstate.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CreateInteresadoInteresaDto {

    private String nombre,apellidos,direccion,email,telefono,avatar;
    private Date createdDate;
    private String mensaje;
    private Long viviendaId;
}
