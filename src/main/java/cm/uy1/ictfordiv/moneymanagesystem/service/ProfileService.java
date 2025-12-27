package cm.uy1.ictfordiv.moneymanagesystem.service;

import cm.uy1.ictfordiv.moneymanagesystem.dto.AuthDTO;
import cm.uy1.ictfordiv.moneymanagesystem.dto.ProfileDTO;
import cm.uy1.ictfordiv.moneymanagesystem.entity.ProfileEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ProfileService {

  ProfileDTO registerProfile(ProfileDTO profileDTO);

  boolean activateProfile(String activationToken);

  boolean isAccountActivated(String email);

  ProfileEntity getCurrentProfile();

  ProfileDTO getPublicProfile(String email);

  Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO);
}
