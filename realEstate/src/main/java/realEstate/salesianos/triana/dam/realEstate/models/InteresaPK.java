package realEstate.salesianos.triana.dam.realEstate.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InteresaPK implements Serializable {

    private Long usuario_id;

    private Long vivienda_id;

}
