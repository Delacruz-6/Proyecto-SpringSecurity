package realEstate.salesianos.triana.dam.realEstate.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import realEstate.salesianos.triana.dam.realEstate.dtos.GetViviendaDto;
import realEstate.salesianos.triana.dam.realEstate.models.Vivienda;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

//Extiende a JpaSpecificationExecutor para poder filtrar por argumentos, las especificaciones están
//en el servicio.
public interface ViviendaRepository extends JpaRepository<Vivienda,Long>, JpaSpecificationExecutor<Vivienda> {

    //Encontrar todos (findAll) usando el grafo de entidad, se podría usar para mostrar todos
    //los detalles de una vivienda.
    @EntityGraph("grafo-vivienda-inmobiliaria-propietario")
    List<Vivienda> findByIdNotNull();

    @Query(value = """
            SELECT COUNT(id), v.*
            FROM VIVIENDA v JOIN INTERESA i ON (i.VIVIENDA_ID = v.ID)
            WHERE i.VIVIENDA_ID=v.ID
            GROUP BY v.ID
            ORDER BY COUNT(id) DESC, v.NUMHABITACIONES DESC
            LIMIT :limit
            """, nativeQuery = true)
    List<Vivienda> findAllTopViviendas (@Param("limit") int limit);
/*
    @Query (value = """
            SELECT v.*
            FROM VIVIENDA v JOIN USUARIO p ON (v.PROPIETARIO.ID = p.ID)
            WHERE v.PROPIETARIO.ID=p.ID
            AND v.ID=id
            GROUP BY v.ID
            """, nativeQuery=true)

 */
    Optional<Vivienda> findByPropietario_Id(Long id);
    //List<Vivienda> findTop10ByOrderByInteresesDesc();
}
