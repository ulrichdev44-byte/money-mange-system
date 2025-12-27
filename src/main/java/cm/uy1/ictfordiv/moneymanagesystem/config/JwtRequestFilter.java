package cm.uy1.ictfordiv.moneymanagesystem.config;

import cm.uy1.ictfordiv.moneymanagesystem.util.JwtUtil;
import jakarta.persistence.Column;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final UserDetailsService userService;
    private final JwtUtil jwtUtil;


    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     * @description
     * Son rôle fondamental
     * Intercepter CHAQUE requête HTTP entrante, vérifier s’il existe un
     * JWT valide, et authentifier automatiquement l’utilisateur
     * dans le contexte de sécurité Spring sans repasser par /login.
     * Autrement dit :
     *
     * /login → crée le JWT
     *
     * JwtRequestFilter → reconnaît le JWT sur chaque requête suivante
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        // lecture du header C’est le seul endroit où le
        // token est transmis dans une API REST sécurisée
        final String authHeader = request.getHeader("Authorization");
        String email = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) { // verifi si le token est bien present
            jwt = authHeader.substring(7); // supprime le mots Bearer pour le garder que le Token
            email = jwtUtil.extractUsername(jwt); // on extrai les informations
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) { // verifier si l'utilisateur etait deja authentifier
            // loading user
            UserDetails userDetails =  this.userService.loadUserByUsername(email); // chargement de l'utilisateur

//            Cette méthode vérifie :
//            signature JWT (clé secrète)
//            expiration
//            correspondance utilisateur ↔ token
//            token non modifié
            if (jwtUtil.validateToken(jwt,userDetails)) {

//                Explication
//                principal → utilisateur authentifié
//                credentials → null (mot de passe inutile)
//                authorities → rôles/permissions
//                Spring considère maintenant l’utilisateur comme authentifié
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                //Ajout des détails de la requête
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);//Injection dans le SecurityContext
            }
        }

        filterChain.doFilter(request, response); // transmission de la requette aux autre filter puis aux controllers
    }



}
