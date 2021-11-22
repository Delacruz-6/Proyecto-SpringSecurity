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

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Usuario  implements UserDetails{

    @Id
    @GeneratedValue
    private Long id;

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

    @OneToMany(mappedBy = "usuario")
    private List<Vivienda> viviendas = new ArrayList<>();


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



}
