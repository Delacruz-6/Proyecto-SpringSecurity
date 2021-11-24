package realEstate.salesianos.triana.dam.realEstate.users.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import realEstate.salesianos.triana.dam.realEstate.models.Inmobiliaria;
import realEstate.salesianos.triana.dam.realEstate.services.InmobiliariaService;
import realEstate.salesianos.triana.dam.realEstate.services.base.BaseService;
import realEstate.salesianos.triana.dam.realEstate.users.dtos.usuario.CreatedUserDto;
import realEstate.salesianos.triana.dam.realEstate.users.dtos.gestor.CreatedGestorDto;
import realEstate.salesianos.triana.dam.realEstate.users.models.UserRole;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;
import realEstate.salesianos.triana.dam.realEstate.users.repositories.UsuarioRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UsuarioService extends BaseService<Usuario, Long, UsuarioRepository> implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final InmobiliariaService inmobiliariaService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.repositorio.findFirstByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(email + " no encontrado"));
    }

    public Page<Usuario> loadUserByRol(UserRole rol , Pageable pageable) throws UsernameNotFoundException {
        return this.repositorio.findByRol(rol, pageable);
    }

    public UserRole loadUserByRolB(UserRole rol ) throws UsernameNotFoundException {
        return this.repositorio.findByRol(rol);
    }

    public Optional<Usuario> loadUserById(Long id,UserRole rol ) throws UsernameNotFoundException{
        return this.repositorio.findByIdAndRol( id, rol);
    }



    public Optional<Usuario> loadUserById2(Long id ) throws UsernameNotFoundException{
        return this.repositorio.findById(id);
    }




    public Usuario savePropietario(CreatedUserDto newUser) {
        if (newUser.getPassword().contentEquals(newUser.getPassword2())) {
            Usuario usuario = Usuario.builder()
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .avatar(newUser.getAvatar())
                    .nombre(newUser.getNombre())
                    .apellidos(newUser.getApellidos())
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
                    .nombre(newUser.getNombre())
                    .apellidos(newUser.getApellidos())
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


    public Usuario saveGestor(CreatedGestorDto newUser) {
        if (newUser.getPassword().contentEquals(newUser.getPassword2())) {
            Usuario usuario = Usuario.builder()
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .avatar(newUser.getAvatar())
                    .nombre(newUser.getNombre())
                    .apellidos(newUser.getApellidos())
                    .email(newUser.getEmail())
                    .direccion(newUser.getDireccion())
                    .telefono(newUser.getTelefono())
                    .rol(UserRole.GESTOR)
                    .build();
            /*
            Optional<Inmobiliaria> inmobiliaria = inmobiliariaService.findById(newUser.getIdInmobiliaria());
            if (inmobiliaria.isPresent()){
                usuario.addInmobiliaria(inmobiliaria.get());
            } else{
                usuario.addInmobiliaria(null);
            }
             */
            return save(usuario);

        } else {
            return null;
        }
    }




}
