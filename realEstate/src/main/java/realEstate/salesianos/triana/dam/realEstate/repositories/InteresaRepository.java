package realEstate.salesianos.triana.dam.realEstate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import realEstate.salesianos.triana.dam.realEstate.models.Interesa;

import java.util.UUID;

public interface InteresaRepository extends JpaRepository<Interesa,Long> {

    //Devuelve un interesa dada su clave primaria compuesta (vivienda_id,usuario_id)
    Interesa findByViviendaIdAndUsuarioId(Long id1, Long id2);
}
