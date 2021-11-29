package realEstate.salesianos.triana.dam.realEstate.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetViviendaToInteresadoDto {

    private Long id;
    private String titulo;
    private double precio;
    private String provincia;
    private String avatar;
    private String tipo;
    private String ciudad;
    private String direccion;
    private String interesUsuario;

    public GetViviendaToInteresadoDto(String titulo, double precio, String provincia, String avatar, String tipo, String ciudad, String direccion, String interesUsuario) {
        this.titulo = titulo;
        this.precio = precio;
        this.provincia = provincia;
        this.avatar = avatar;
        this.tipo = tipo;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.interesUsuario = interesUsuario;
    }
}
