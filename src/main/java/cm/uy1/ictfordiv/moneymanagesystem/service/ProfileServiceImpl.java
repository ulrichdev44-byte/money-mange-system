package cm.uy1.ictfordiv.moneymanagesystem.service;

import cm.uy1.ictfordiv.moneymanagesystem.dto.AuthDTO;
import cm.uy1.ictfordiv.moneymanagesystem.dto.ProfileDTO;
import cm.uy1.ictfordiv.moneymanagesystem.entity.ProfileEntity;
import cm.uy1.ictfordiv.moneymanagesystem.mapper.ProfileMappers;
import cm.uy1.ictfordiv.moneymanagesystem.repository.ProfileRepository;
import cm.uy1.ictfordiv.moneymanagesystem.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final SendMailService sendMailService;
    private final ProfileMappers profileMappers;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private  final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Value("${money.manager.backend.url}")
    private String activationURL;
    @Override
    public ProfileDTO registerProfile(ProfileDTO profileDTO) {
        profileDTO.setActivationToken(UUID.randomUUID().toString());
        profileDTO.setPassword(passwordEncoder.encode(profileDTO.getPassword()));
        ProfileEntity newProfileEntity = profileMappers.toProfileEntity(profileDTO);
        profileRepository.save(newProfileEntity);

        String activationLink = activationURL+"/api/v1.0/activate?token="+newProfileEntity.getActivationToken();
        String subject = "Activate your money manager account";
        String body = "Click here to activate your money manager account"+activationLink;

       sendMailService.sendMail(newProfileEntity.getEmail(), subject, body);

        return profileMappers.toProfileDTO(newProfileEntity);
    }

    @Override
    public boolean activateProfile(String activationToken) {
        return profileRepository.findByActivationToken(activationToken)
                .map(profile ->{
                    profile.setActive(true);
                    profileRepository.save(profile);
                    return true;
                }).orElse(false);
    }
    /*
    * verifier si le compte est active
    * */

    @Override
    public boolean isAccountActivated(String email) {
        return profileRepository.findByEmail(email)
                .map(ProfileEntity::isActive)
                .orElse(false);
    }

    /**
     * @return
     */
    @Override
    public ProfileEntity getCurrentProfile() {
        // getting credentials for current user authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // get current name
        String email =  authentication.getName();

        return profileRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found with Email:  "+email));
    }

    /**
     * @param email
     * @return
     */
    @Override
    public ProfileDTO getPublicProfile(String email) {

        ProfileEntity currentProfile = null;
        if (email == null){
            // add for current data user authenticated
            currentProfile = this.getCurrentProfile();
        }else {
            // get profilr from database
            currentProfile = profileRepository.findByEmail(email)
                    .orElseThrow(()-> new UsernameNotFoundException("User Not Found with Email:  "+email));
        }
        // set founded user into ProfileDTO and return
        return ProfileDTO.builder()
                .id(currentProfile.getId())
                .fullname(currentProfile.getFullname())
                .email(currentProfile.getEmail())
                .imageUrl(currentProfile.getImageUrl())
                .createdAt(currentProfile.getCreatedAt())
                .updatedAt(currentProfile.getUpdatedAt())
                .build();
    }

    /**
     * @param authDTO
     * @return
     */
    @Override
    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO) {

        try {
            // 1. Authentification via Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authDTO.getEmail(),
                            authDTO.getPassword()
                    )
            );

            // 2. Sécurité supplémentaire
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. Récupération du principal
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 4. Génération du vrai JWT
            String jwtToken = jwtUtil.generateToken(userDetails);

            // 5. Réponse
            return Map.of(
                    "user", this.getPublicProfile(userDetails.getUsername()),
                    "token", jwtToken
            );

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid email or password");
        } catch (DisabledException e) {
            throw new RuntimeException("Account is disabled");
        }
    }


}
