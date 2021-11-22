package realEstate.salesianos.triana.dam.realEstate.models;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;

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
    @MapsId("usuario_id")
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @MapsId("vivienda_id")
    @JoinColumn(name = "vivienda_id")
    private Vivienda vivienda;


    /*
     HELPERS
     */
    public void addToUsuario (Usuario u){
        usuario = u;
        if (u.getIntereses() == null){
            u.setIntereses(new ArrayList<>());
            u.getIntereses().add(this);
        }
    }
    public void removeFromUsuario (Usuario u){
        u.getIntereses().remove(this);
        usuario = null;
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