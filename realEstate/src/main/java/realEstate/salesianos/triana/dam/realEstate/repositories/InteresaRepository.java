package realEstate.salesianos.triana.dam.realEstate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import realEstate.salesianos.triana.dam.realEstate.models.Interesa;

import java.util.List;
import java.util.UUID;

public interface InteresaRepository extends JpaRepository<Interesa,Long> {

    //Devuelve un interesa dada su clave primaria compuesta (vivienda_id,usuario_id)
    Interesa findByViviendaIdAndUsuarioId(Long id1, Long id2);

    @Query(value = """
            SELECT int.* INTERESA int
            WHERE int.interesado_id IN
            (SELECT interesado_id FROM Interesa i
            GROUP BY interesado_id
            """, nativeQuery = true)
    List<Interesa> findIntersados();

}
