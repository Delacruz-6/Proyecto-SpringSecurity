package realEstate.salesianos.triana.dam.realEstate.users.models;

import lombok.*;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import realEstate.salesianos.triana.dam.realEstate.models.Inmobiliaria;
import realEstate.salesianos.triana.dam.realEstate.models.Interesa;
import realEstate.salesianos.triana.dam.realEstate.models.Vivienda;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Usuario  implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
        @Id
    private UUID id = UUID.randomUUID();
     */



    @NaturalId
    @Column(unique = true, updatable = false)
    private String email;

    private String password;

    private String avatar;

    private String nombre, apellidos,direccion;

    private Integer telefono;

    private UserRole rol;

    @CreatedDate
    private LocalDateTime createAt;

    @Builder.Default
    private LocalDateTime lastPasswordChangeAt = LocalDateTime.now();

    @Builder.Default
    @OneToMany(mappedBy = "usuario")
    private List<Interesa> intereses = new ArrayList<>();


    @JoinColumn(name = "inmobiliaria_id", foreignKey = @ForeignKey(name = "FK_USUARIO_INMOBILIARIA"))
    @ManyToOne
    private Inmobiliaria inmobiliaria;

    @Builder.Default
    @OneToMany(mappedBy = "propietario")
    private List<Vivienda> viviendas = new ArrayList<>();

    public Usuario(String nombre, String apellidos) {
        this.nombre = nombre;
        this.apellidos = apellidos;
    }


    public Usuario(String email, String avatar, String nombre, String apellidos, String direccion, Integer telefono) {
        this.email = email;
        this.avatar = avatar;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //HELPERS
    @PreRemove
    public void nullearPropietarioDeViviendas(){
        viviendas.forEach(vivienda -> vivienda.setPropietario(null));
    }

    /*
    public Usuario() {
        id = UUID.randomUUID();
    }
     */


    public void addInmobiliaria( Inmobiliaria i){
        this.inmobiliaria= i;
        i.getGestores().add(this);

    }
    public void deleteInmobiliaria ( Inmobiliaria i){
        i.getGestores().remove(this);
        this.inmobiliaria = null;
    }





}
