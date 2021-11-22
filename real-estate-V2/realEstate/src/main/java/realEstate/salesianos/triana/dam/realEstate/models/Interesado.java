package realEstate.salesianos.triana.dam.realEstate.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity @SuperBuilder
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Interesado extends Persona{

    @Builder.Default
    @OneToMany(mappedBy = "interesado")
    private List<Interesa> intereses = new ArrayList<>();


    public Interesado(String nombre, String apellidos, String direccion, String email, String telefono, String avatar) {
        super(nombre, apellidos, direccion, email, telefono, avatar);
    }

}
