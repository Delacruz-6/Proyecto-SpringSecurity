package realEstate.salesianos.triana.dam.realEstate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import realEstate.salesianos.triana.dam.realEstate.dtos.*;
import realEstate.salesianos.triana.dam.realEstate.models.Tipo;
import realEstate.salesianos.triana.dam.realEstate.models.Vivienda;
import realEstate.salesianos.triana.dam.realEstate.repositories.ViviendaRepository;
import realEstate.salesianos.triana.dam.realEstate.services.base.BaseService;
import realEstate.salesianos.triana.dam.realEstate.users.dtos.gestor.CreatedGestorDto;
import realEstate.salesianos.triana.dam.realEstate.users.models.UserRole;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ViviendaService extends BaseService<Vivienda,Long,ViviendaRepository> {
    private final InteresaDtoConverter interesaDtoConverter = new InteresaDtoConverter();
    private final PropietarioDtoConverter propietarioDtoConverter = new PropietarioDtoConverter();
    private final InmobiliariaDtoConverter inmobiliariaDtoConverter = new InmobiliariaDtoConverter();
    @Autowired
    private  InmobiliariaService inmobiliariaService ;

    public Optional<Usuario> findPropietario (Long id){
        return repositorio.findByPropietarioId(id);
    }

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

    public Vivienda saveVivienda(CreateViviendaDto newVivienda) {
            Vivienda vivienda = Vivienda.builder()
                    .titulo(newVivienda.getTitulo())
                    .descripcion(newVivienda.getDescripcion())
                    .direccion(newVivienda.getDireccion())
                    .metrosCuadrados(newVivienda.getMetrosCuadrados())
                    .codigoPostal(newVivienda.getCodigoPostal())
                    .numBanos(newVivienda.getNumBanos())
                    .numHabitaciones(newVivienda.getNumHabitaciones())
                    .precio(newVivienda.getPrecio())
                    .avatar(newVivienda.getAvatar())
                    .provincia(newVivienda.getProvincia())
                    .poblacion(newVivienda.getCiudad())
                    .tieneGaraje(newVivienda.isTieneGaraje())
                    .tieneAscensor(newVivienda.isTieneAscensor())
                    .tienePiscina(newVivienda.isTienePiscina())
                    .latitudLongitud(newVivienda.getLatitudLongitud())
                    .build();
            return save(vivienda);

    }

    public Vivienda saveHouse  (CreateViviendaDto v) {

        Vivienda result = new Vivienda();
        Tipo t = Tipo.valueOf(v.getTipo());
        //result.setId(v.getId());
        result.setTitulo(v.getTitulo());
        result.setDescripcion(v.getDescripcion());
        result.setAvatar(v.getAvatar());
        result.setLatitudLongitud(v.getLatitudLongitud());
        result.setDireccion(v.getDireccion());
        result.setCodigoPostal(v.getCodigoPostal());
        result.setPoblacion(v.getCiudad());
        result.setProvincia(v.getProvincia());
        result.setTipo(t);
        result.setPrecio(v.getPrecio());
        result.setNumHabitaciones(v.getNumHabitaciones());
        result.setMetrosCuadrados(v.getMetrosCuadrados());
        result.setNumBanos(v.getNumBanos());
        result.setTienePiscina(v.isTienePiscina());
        result.setTieneAscensor(v.isTieneAscensor());
        result.setTieneGaraje(v.isTieneGaraje());


        return result;
    }
}
