package realEstate.salesianos.triana.dam.realEstate.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class GetViviendaDto {
    private Long id;
    private String titulo;
    private double precio;
    private String provincia;
    private String avatar;
    private String tipo;
    private String ciudad;
    private String direccion;
    private int numHabitaciones;
    private int metrosCuadrados;
    private int meInteresas;

    public GetViviendaDto(Long id, String titulo, double precio, String provincia, String avatar, String tipo, String ciudad, String direccion, int numHabitaciones, int metrosCuadrados, int meInteresas) {
        this.id = id;
        this.titulo = titulo;
        this.precio = precio;
        this.provincia = provincia;
        this.avatar = avatar;
        this.tipo = tipo;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.numHabitaciones = numHabitaciones;
        this.metrosCuadrados = metrosCuadrados;
        this.meInteresas = meInteresas;
    }

    public GetViviendaDto(Long id, String titulo, double precio, String avatar, String tipo, String ciudad, String direccion, int numHabitaciones, int metrosCuadrados, int meInteresas) {
    }
}
