package cm.uy1.ictfordiv.moneymanagesystem.mapper;

import cm.uy1.ictfordiv.moneymanagesystem.dto.ProfileDTO;
import cm.uy1.ictfordiv.moneymanagesystem.entity.ProfileEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Data
@Service
@RequiredArgsConstructor
public class ProfileMappers {

    public final PasswordEncoder passwordEncoder;

    public ProfileDTO toProfileDTO(ProfileEntity profileEntity) {
        ProfileDTO profileDTO = new ProfileDTO();

        profileDTO.setPassword(passwordEncoder.encode(profileEntity.getPassword()));
        BeanUtils.copyProperties(profileEntity, profileDTO);
        return profileDTO;
    }

    public ProfileEntity toProfileEntity(ProfileDTO profileDTO) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setPassword(passwordEncoder.encode(profileDTO.getPassword()));
        BeanUtils.copyProperties(profileDTO, profileEntity);
        return profileEntity;
    }

}
