package cm.uy1.ictfordiv.moneymanagesystem.repository;

import cm.uy1.ictfordiv.moneymanagesystem.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    Optional<ProfileEntity> findByEmail(String email);

    // select * from tbl_profile where acticationToken = ?
    Optional<ProfileEntity> findByActivationToken(String activationToken);
}
