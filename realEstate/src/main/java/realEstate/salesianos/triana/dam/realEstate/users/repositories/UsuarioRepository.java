package realEstate.salesianos.triana.dam.realEstate.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;

import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findFirstByEmail(String email);

}
