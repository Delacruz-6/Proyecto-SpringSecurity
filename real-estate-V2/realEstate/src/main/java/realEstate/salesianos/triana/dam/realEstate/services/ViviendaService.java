package realEstate.salesianos.triana.dam.realEstate.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import realEstate.salesianos.triana.dam.realEstate.dtos.GetViviendaDto;
import realEstate.salesianos.triana.dam.realEstate.models.Tipo;
import realEstate.salesianos.triana.dam.realEstate.models.Vivienda;
import realEstate.salesianos.triana.dam.realEstate.repositories.ViviendaRepository;
import realEstate.salesianos.triana.dam.realEstate.services.base.BaseService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ViviendaService extends BaseService<Vivienda,Long,ViviendaRepository> {

    //Cuando se seleccione una vivienda para ver sus detalles se usará este método.
    @EntityGraph("grafo-vivienda-inmobiliaria-propietario")
    public Optional<Vivienda> encontrarViviendaPorId(Long id){
        return repositorio.findById(id);
    }
    public List<Vivienda> findAllByInteresas (int limit) {
        return repositorio.findAllTopViviendas(limit);}

    //Método para generar la especificación de filtrado.

    public Page<Vivienda> findByArgs (final Optional<String> ciudad,
                                      final Optional<Float> precioMax,
                                      final Optional<Float> precioMin,
                                      final Optional<String> provincia,
                                      final Optional<Float> tamanio,
                                      final Optional<Float> numHabs,
                                      final Optional<Tipo> tipo,
                                      Pageable pageable) {
        String vacio="";

        Specification<Vivienda> specCiudadVivienda = new Specification<Vivienda>() {
            @Override
            public Predicate toPredicate(Root<Vivienda> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (ciudad.isPresent()){
                    return criteriaBuilder.like(criteriaBuilder.lower(root.get("poblacion")),"%" + ciudad.get().toLowerCase() + "%");
                } else {
                    return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                }
            }
        };

        Specification<Vivienda> precioMenorQue = new Specification<Vivienda>() {
            @Override
            public Predicate toPredicate(Root<Vivienda> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (precioMax.isPresent()){
                    return criteriaBuilder.lessThanOrEqualTo(root.get("precio"), precioMax.get());
                } else {
                    return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                }

            }
        };

        Specification<Vivienda> precioMayorQue = new Specification<Vivienda>() {
            @Override
            public Predicate toPredicate(Root<Vivienda> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (precioMin.isPresent()){
                    return criteriaBuilder.greaterThanOrEqualTo(root.get("precio"), precioMin.get());
                } else {
                    return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                }

            }
        };

        Specification<Vivienda> filtradoProvincia = new Specification<Vivienda>() {
            @Override
            public Predicate toPredicate(Root<Vivienda> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (provincia.isPresent()){
                    return criteriaBuilder.like(criteriaBuilder.lower(root.get("provincia")),"%" + provincia.get().toLowerCase() + "%");
                } else {
                    return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                }
            }
        };

        Specification<Vivienda> tamanioMetros = new Specification<Vivienda>() {
            @Override
            public Predicate toPredicate(Root<Vivienda> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (tamanio.isPresent()){
                    return criteriaBuilder.lessThanOrEqualTo((root.get("metrosCuadrados")), tamanio.get());
                } else {
                    return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                }

            }
        };

        Specification<Vivienda> filtroHabs = new Specification<Vivienda>() {
            @Override
            public Predicate toPredicate(Root<Vivienda> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (numHabs.isPresent()){
                    return criteriaBuilder.equal(root.get("numHabitaciones"), numHabs.get());
                } else {
                    return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                }

            }
        };

        Specification<Vivienda> filtroTipo = new Specification<Vivienda>() {
            @Override
            public Predicate toPredicate(Root<Vivienda> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (tipo.isPresent()){
                    return criteriaBuilder.equal(root.get("tipo"), tipo.get());
                } else {
                    return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
                }

            }
        };
            Specification<Vivienda> todas = specCiudadVivienda.and(precioMenorQue).and(precioMayorQue).and(filtradoProvincia).and(tamanioMetros).and(filtroHabs).and(filtroTipo);

        return this.repositorio.findAll(todas,pageable);

    }
}
