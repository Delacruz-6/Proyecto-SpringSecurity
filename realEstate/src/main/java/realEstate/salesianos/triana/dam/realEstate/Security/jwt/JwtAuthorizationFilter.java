package realEstate.salesianos.triana.dam.realEstate.Security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import realEstate.salesianos.triana.dam.realEstate.users.models.Usuario;
import realEstate.salesianos.triana.dam.realEstate.users.services.UsuarioService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Log
@RequiredArgsConstructor
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {


    private final UsuarioService userService;
    private final JwtTokenProvider jwtProvider;

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtTokenProvider.TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtTokenProvider.TOKEN_PREFIX)) {
            return bearerToken.substring(JwtTokenProvider.TOKEN_PREFIX.length());
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request , HttpServletResponse response , FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromRequest(request);
        try {
            if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {

                Long userId = jwtProvider.getUserIdFromJwt(token);

                Optional<Usuario> usuarioOptional = userService.findById(userId);

                if (usuarioOptional.isPresent()) {
                    Usuario user = usuarioOptional.get();
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    user.getRol(),
                                    user.getAuthorities()
                            );
                    authentication.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        } catch (Exception ex) {
            log.info("No se ha podido establecer el contexto de seguridad (" + ex.getMessage() + ")");
        }
        filterChain.doFilter(request, response);
    }
}
