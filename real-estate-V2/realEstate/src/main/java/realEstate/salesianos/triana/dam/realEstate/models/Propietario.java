package realEstate.salesianos.triana.dam.realEstate.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("PP")
public class Propietario extends Persona{


    @OneToMany(mappedBy = "propietario")
    private List<Vivienda> viviendas = new ArrayList<>();

    public Propietario(String nombre, String apellidos) {
        super(nombre, apellidos);
    }

    //HELPERS
    @PreRemove
    public void nullearPropietarioDeViviendas(){
        viviendas.forEach(vivienda -> vivienda.setPropietario(null));
    }

}
