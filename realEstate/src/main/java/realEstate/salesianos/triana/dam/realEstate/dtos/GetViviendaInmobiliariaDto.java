package realEstate.salesianos.triana.dam.realEstate.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class GetViviendaInmobiliariaDto  extends GetViviendaDto{
    private String nombreInmobiliaria;

    private Long idInmobiliaria;

    public GetViviendaInmobiliariaDto(Long id, String titulo, double precio, String avatar, String tipo, String ciudad, String direccion, int numHabitaciones, int metrosCuadrados, int meInteresas, String nombreInmobiliaria, Long idInmobiliaria) {
        super(id, titulo, precio, avatar, tipo, ciudad, direccion, numHabitaciones, metrosCuadrados, meInteresas);
        this.nombreInmobiliaria = nombreInmobiliaria;
        this.idInmobiliaria = idInmobiliaria;
    }
}
