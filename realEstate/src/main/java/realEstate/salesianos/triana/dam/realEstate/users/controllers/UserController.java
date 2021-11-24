package realEstate.salesianos.triana.dam.realEstate.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import realEstate.salesianos.triana.dam.realEstate.users.dtos.gestor.GetGestorDto;
import realEstate.salesianos.triana.dam.realEstate.users.dtos.usuario.CreatedUserDto;
import realEstate.salesianos.triana.dam.realEstate.users.dtos.usuario.GetUserDto;
import realEstate.salesianos.triana.dam.realEstate.users.dtos.usuario.UserDtoConverter;
import realEstate.salesianos.triana.dam.realEstate.users.dtos.gestor.CreatedGestorDto;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;
import realEstate.salesianos.triana.dam.realEstate.users.services.UsuarioService;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UsuarioService usuarioService;
    private final UserDtoConverter userDtoConverter;

    @PostMapping ("/auth/register/user")
    public ResponseEntity<GetUserDto> nuevoPropietario(@RequestBody CreatedUserDto newUser) {
        Usuario saved = usuarioService.savePropietario(newUser);
        return buildResponse(saved);
    }

    @PostMapping ("/auth/register/admin")
    public ResponseEntity<GetUserDto> nuevoAdmin(@RequestBody CreatedUserDto newUser) {
        Usuario saved = usuarioService.saveAdmin(newUser);
        return buildResponse(saved);
    }

    @PostMapping ("/auth/register/gestor")
    public ResponseEntity<GetGestorDto> nuevoGestor(@RequestBody CreatedGestorDto newUser) {
        Usuario saved = usuarioService.saveGestor(newUser);
        if (saved == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(userDtoConverter.convertGestorToGetUserDto(saved));
    }




    private ResponseEntity<GetUserDto> buildResponse (Usuario data){
        if (data == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(userDtoConverter.convertUsuarioToGetUserDto(data));
    }
}
