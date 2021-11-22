package realEstate.salesianos.triana.dam.realEstate.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import realEstate.salesianos.triana.dam.realEstate.models.Vivienda;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
public class CreateInmobiliariaDto {

    private String nombre;

    private List<Vivienda> viviendaList;
}
