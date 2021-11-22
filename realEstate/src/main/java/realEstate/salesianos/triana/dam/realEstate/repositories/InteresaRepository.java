package realEstate.salesianos.triana.dam.realEstate.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import realEstate.salesianos.triana.dam.realEstate.models.Interesa;

public interface InteresaRepository extends JpaRepository<Interesa,Long> {

    //Devuelve un interesa dada su clave primaria compuesta (vivienda_id,interesado_id)
    Interesa findByViviendaIdAndInteresadoId(Long id1,Long id2);
}
