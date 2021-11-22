package realEstate.salesianos.triana.dam.realEstate.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class GetPropietarioConViviendasDto extends GetPropietarioDetailsDto{

   // @Builder.Default
    private List<GetViviendaSinPropietarioDto> viviendas = new ArrayList<>();

    public GetPropietarioConViviendasDto(Long id, String nombre, String apellidos, String direccion, String email, String telefono, String avatar, List<GetViviendaSinPropietarioDto> viviendas) {
        super(id, nombre, apellidos, direccion, email, telefono, avatar);
        this.viviendas = viviendas;
    }
}
