package realEstate.salesianos.triana.dam.realEstate.users.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import realEstate.salesianos.triana.dam.realEstate.users.models.UserRole;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;

import java.util.List;
import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findFirstByEmail(String email);

    Page<Usuario> findByRol(UserRole rol, Pageable p);

    @Query (value = "SELECT u.* FROM usuario u WHERE u.rol:rol;", nativeQuery=true)
    List<Usuario> userByRol(String rol);
    

}
