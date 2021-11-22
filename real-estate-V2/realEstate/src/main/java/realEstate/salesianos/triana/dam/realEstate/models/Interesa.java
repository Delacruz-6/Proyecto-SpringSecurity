package realEstate.salesianos.triana.dam.realEstate.models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@SuperBuilder @Entity
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
public class Interesa {

    @Builder.Default
    @EmbeddedId
    private InteresaPK id = new InteresaPK();


    @CreatedDate
    private Date createdDate;

    @Lob
    private  String mensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("interesado_id")
    @JoinColumn(name = "interesado_id")
    private Interesado interesado;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @MapsId("vivienda_id")
    @JoinColumn(name = "vivienda_id")
    private Vivienda vivienda;


    /*
     HELPERS
     */
    public void addToInteresado (Interesado i){
        interesado = i;
        if (i.getIntereses() == null){
            i.setIntereses(new ArrayList<>());
            i.getIntereses().add(this);
        }
    }
    public void removeFromInteresado (Interesado i){
        i.getIntereses().remove(this);
        interesado = null;
    }


    public void addToVivienda (Vivienda v){
        vivienda = v;
        v.getIntereses().add(this);
    }
    public void removeFromVivienda (Vivienda v){
        v.getIntereses().remove(this);
        vivienda = null;
    }





}