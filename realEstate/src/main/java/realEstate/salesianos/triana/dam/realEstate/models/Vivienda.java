package realEstate.salesianos.triana.dam.realEstate.models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@NamedEntityGraph(
        name = "grafo-vivienda-inmobiliaria-propietario",
        attributeNodes = {
                @NamedAttributeNode("inmobiliaria"),
                @NamedAttributeNode("propietario")
        },
        subgraphs = {
                @NamedSubgraph(
                        name= "grafo-vivienda-inmobiliaria",
                        attributeNodes = {@NamedAttributeNode("inmobiliaria")}
                ),
                @NamedSubgraph(
                        name = "grafo-vivienda-propietario",
                        attributeNodes = {@NamedAttributeNode("propietario")}
                )
        }
)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Vivienda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Lob
    private String descripcion;
    private String avatar;
    @Column(name = "LATITUDLONGITUD")
    private String latitudLongitud;
    private String direccion;
    @Column(name = "CODIGOPOSTAL")
    private String codigoPostal;
    private String poblacion;
    private String provincia;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private Tipo tipo;

    private double precio;
    @Column(name = "NUMHABITACIONES")
    private int numHabitaciones;
    @Column(name = "METROSCUADRADOS")
    private int metrosCuadrados;
    @Column(name = "NUMBANOS")
    private int numBanos;
    @Column(name = "TIENEPISCINA")
    private boolean tienePiscina;
    @Column(name = "TIENEASCENSOR")
    private boolean tieneAscensor;
    @Column(name = "TIENEGARAJE")
    private boolean tieneGaraje;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vivienda")
    private List<Interesa> intereses = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inmobiliaria_id", foreignKey = @ForeignKey(name = "FK_VIVIENDA_INMOBILIARIA"))
    private Inmobiliaria inmobiliaria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", foreignKey = @ForeignKey(name = "FK_VIVIENDA_USUARIO"))
    private Usuario propietario;



    public Vivienda(Long id, String titulo, String descripcion, String avatar, String direccion, String poblacion, String provincia, Tipo tipo, double precio, int numHabitaciones, int metrosCuadrados, List<Interesa> intereses) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.avatar = avatar;
        this.direccion = direccion;
        this.poblacion = poblacion;
        this.provincia = provincia;
        this.tipo = tipo;
        this.precio = precio;
        this.numHabitaciones = numHabitaciones;
        this.metrosCuadrados = metrosCuadrados;
        this.intereses = intereses;
    }


//HELPERS

    public void addToInmobiliaria(Inmobiliaria i){
        inmobiliaria = i;
        if (i.getViviendas() == null){
            i.setViviendas(new ArrayList<>());
            i.getViviendas().add(this);
        }
    }

    public void removeFromInmobiliaria(Inmobiliaria i) {
        i.getViviendas().remove(this);
        inmobiliaria = null;
    }

    public void addToPropietario(Usuario u){
        propietario = u;
        if (u.getViviendas() == null){
            u.setViviendas(new ArrayList<>());
            u.getViviendas().add(this);
        }
    }

    public void removeFromPropietario(Usuario u) {
        u.getViviendas().remove(this);
        propietario = null;
    }


    @PreRemove
    public void removeViviendasToIntereses(){
        intereses.forEach(interesa -> interesa.setVivienda(null));
    }
  
}
