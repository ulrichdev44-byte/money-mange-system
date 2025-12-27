package cm.uy1.ictfordiv.moneymanagesystem.config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration  // Indique que c'est une classe de configuration Spring
@RequiredArgsConstructor  // Génère un constructeur avec tous les champs (Lombok)
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    // Configure la chaîne de filtres de sécurité principale
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Active CORS avec les paramètres par défaut
                .cors(Customizer.withDefaults())

                // Désactive CSRF (non nécessaire pour les APIs REST stateless)
                .csrf(AbstractHttpConfigurer::disable)
                // desactive la configuration de base
                .httpBasic(AbstractHttpConfigurer::disable)

                // Configure les autorisations d'accès aux endpoints
                .authorizeHttpRequests(auth -> {
                    // Ces endpoints sont accessibles sans authentification
                    auth.requestMatchers(
                                    "/login",      // Endpoint de connexion
                                    "/create",     // Endpoint de création de compte
                                    "/activate",   // Endpoint d'activation de compte
                                    "/health",     // Endpoint de santé de l'application
                                    "/status"      // Endpoint de statut
                            ).permitAll()      // Permet l'accès libre

                            // Toutes les autres requêtes nécessitent une authentification
                            .anyRequest().authenticated();
                })

                // Configure la gestion des sessions comme stateless (pas de session)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // Construit et retourne la configuration de sécurité
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource (){

        CorsConfiguration corsConfiguration = new CorsConfiguration(); // declaration de cors

        corsConfiguration.setAllowedOriginPatterns(List.of("*")); // permet de definir le api qui aurons access
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type","Accept"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();// cree un registre ou on peu stocker tous les regles cors
        source.registerCorsConfiguration("/**", corsConfiguration);// enregistre mes regle cors pour tous mes routes

        return source;
    }
// fonction pour generer un token
    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);

    }
}
