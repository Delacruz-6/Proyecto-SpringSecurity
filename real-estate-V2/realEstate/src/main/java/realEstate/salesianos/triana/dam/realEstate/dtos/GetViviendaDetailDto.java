package realEstate.salesianos.triana.dam.realEstate.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import realEstate.salesianos.triana.dam.realEstate.models.Inmobiliaria;
import realEstate.salesianos.triana.dam.realEstate.models.Interesa;
import realEstate.salesianos.triana.dam.realEstate.models.Interesado;
import realEstate.salesianos.triana.dam.realEstate.models.Propietario;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetViviendaDetailDto {

    private Long id;
    private String titulo;
    private String descripcion;
    private String avatar;
    private String latitudLongitud;
    private String direccion;
    private String codigoPostal;
    private String ciudad;
    private String provincia;
    private String tipo;
    private double precio;
    private int numHabitaciones;
    private int metrosCuadrados;
    private int numBanos;
    private boolean tienePiscina;
    private boolean tieneAscensor;
    private boolean tieneGaraje;
    private GetPropietarioDetailsDto propietario;
    private GetInmobiliariaDetailDto inmobiliaria;
    private int meInteresas;
    private List<GetInteresaDto> interesas = new ArrayList<>();
}
