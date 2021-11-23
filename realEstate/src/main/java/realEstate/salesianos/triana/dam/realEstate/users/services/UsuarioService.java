package realEstate.salesianos.triana.dam.realEstate.users.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import realEstate.salesianos.triana.dam.realEstate.services.base.BaseService;
import realEstate.salesianos.triana.dam.realEstate.users.dtos.CreatedUserDto;
import realEstate.salesianos.triana.dam.realEstate.users.models.UserRole;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;
import realEstate.salesianos.triana.dam.realEstate.users.repositories.UsuarioRepository;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UsuarioService extends BaseService<Usuario, Long, UsuarioRepository> implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.repositorio.findFirstByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email + " no encontrado"));
    }

    public Page<Usuario> loadUserByRol(UserRole rol , Pageable pageable) throws UsernameNotFoundException {
        return this.repositorio.findByRol(rol, pageable);
    }

    public Optional<Usuario> loadUserById(Long id) throws UsernameNotFoundException{
        return this.repositorio.findById(id);
    }



    public Usuario savePropietario(CreatedUserDto newUser) {
        if (newUser.getPassword().contentEquals(newUser.getPassword2())) {
            Usuario usuario = Usuario.builder()
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .avatar(newUser.getAvatar())
                    .nombre(newUser.getNombre())
                    .email(newUser.getEmail())
                    .direccion(newUser.getDireccion())
                    .telefono(newUser.getTelefono())
                    .rol(UserRole.PROPIETARIO)
                    .build();
            return save(usuario);
        } else {
            return null;
        }
    }

    public Usuario saveAdmin(CreatedUserDto newUser) {
        if (newUser.getPassword().contentEquals(newUser.getPassword2())) {
            Usuario usuario = Usuario.builder()
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .avatar(newUser.getAvatar())
                    .nombre(String.format("%s, %s", newUser.getApellidos(), newUser.getNombre()))
                    .email(newUser.getEmail())
                    .direccion(newUser.getDireccion())
                    .telefono(newUser.getTelefono())
                    .rol(UserRole.ADMIN)
                    .build();
            return save(usuario);
        } else {
            return null;
        }
    }


    public Usuario saveGestor(CreatedUserDto newUser) {
        if (newUser.getPassword().contentEquals(newUser.getPassword2())) {
            Usuario usuario = Usuario.builder()
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .avatar(newUser.getAvatar())
                    .nombre(String.format("%s, %s", newUser.getApellidos(), newUser.getNombre()))
                    .email(newUser.getEmail())
                    .direccion(newUser.getDireccion())
                    .telefono(newUser.getTelefono())
                    .rol(UserRole.GESTOR)
                    .build();
            return save(usuario);
        } else {
            return null;
        }
    }




}
