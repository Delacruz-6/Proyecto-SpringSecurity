package realEstate.salesianos.triana.dam.realEstate.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import realEstate.salesianos.triana.dam.realEstate.users.dtos.CreatedUserDto;
import realEstate.salesianos.triana.dam.realEstate.users.dtos.GetUserDto;
import realEstate.salesianos.triana.dam.realEstate.users.dtos.UserDtoConverter;
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

    @PreAuthorize ("hasRole('ADMIN')")
    @PostMapping ("/auth/register/admin")
    public ResponseEntity<GetUserDto> nuevoAdmin(@RequestBody CreatedUserDto newUser) {
        Usuario saved = usuarioService.saveAdmin(newUser);
        return buildResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping ("/auth/register/gestor")
    public ResponseEntity<GetUserDto> nuevoGestor(@RequestBody CreatedUserDto newUser) {
        Usuario saved = usuarioService.saveGestor(newUser);
        return buildResponse(saved);
    }




    private ResponseEntity<GetUserDto> buildResponse (Usuario data){
        if (data == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(userDtoConverter.convertUserEntityToGetUserDto(data));
    }
}
