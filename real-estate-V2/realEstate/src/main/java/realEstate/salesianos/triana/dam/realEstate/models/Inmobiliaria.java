package realEstate.salesianos.triana.dam.realEstate.models;

import lombok.*;

import javax.persistence.*;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/*@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "grafo-inmobiliaria-vivienda",
                attributeNodes = {
                        @NamedAttributeNode("Vivienda"),
                }
        )
})*/

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Inmobiliaria implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String email;
    private String telefono;
    private String avatar;

    @OneToMany(mappedBy = "inmobiliaria", fetch = FetchType.LAZY)
    private List<Vivienda> viviendas = new ArrayList<>();

    public Inmobiliaria(String nombre, String avatar) {
        this.nombre = nombre;
        this.avatar = avatar;
    }
    @PreRemove
    public void nullearInmobiliariaDeViviendas(){
        viviendas.forEach(vivienda -> vivienda.setInmobiliaria(null));
    }


}
