package realEstate.salesianos.triana.dam.realEstate.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import realEstate.salesianos.triana.dam.realEstate.models.Vivienda;

import java.util.List;

@SuperBuilder
@Getter @Setter
@NoArgsConstructor
public class GetInmobiliariaViviendasDto extends GetInmobiliariaDto{

    private List<String> nombreVivienda;

    public GetInmobiliariaViviendasDto(Long id, String nombre, String avatar, int numViviendas, List<String> nombreVivienda) {
        super(id, nombre, avatar, numViviendas);
        this.nombreVivienda = nombreVivienda;
    }
}
