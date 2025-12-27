package cm.uy1.ictfordiv.moneymanagesystem.controller;

import cm.uy1.ictfordiv.moneymanagesystem.dto.AuthDTO;
import cm.uy1.ictfordiv.moneymanagesystem.dto.ProfileDTO;
import cm.uy1.ictfordiv.moneymanagesystem.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/create")
    public ResponseEntity<ProfileDTO> createProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO registredProfile = profileService.registerProfile(profileDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(registredProfile);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateProfile(@RequestParam String token){

        boolean activated = profileService.activateProfile(token);

        if (activated == true){return ResponseEntity.ok("profile actvates successfully!");
        }else{return ResponseEntity.status(HttpStatus.NOT_FOUND).body("profile is not activated!");}

    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDTO authDTO) {
        try {
            // ckeck out if a user account has been activated
            if (!profileService.isAccountActivated(authDTO.getEmail())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Account is not activated! please activate it first!"));
            }
            Map<String, Object> response = profileService.authenticateAndGenerateToken(authDTO);

         return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/test")
    public String test(){
    return "test";
    }
}
