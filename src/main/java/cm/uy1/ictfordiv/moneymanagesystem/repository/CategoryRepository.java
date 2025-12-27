package cm.uy1.ictfordiv.moneymanagesystem.repository;

import cm.uy1.ictfordiv.moneymanagesystem.dto.CategoryDTO;
import cm.uy1.ictfordiv.moneymanagesystem.entity.CategoryEntity;
import cm.uy1.ictfordiv.moneymanagesystem.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    // select * from tbl_Category where id = ?1
    List<CategoryEntity> findAllById(Long id);

    List<CategoryEntity>  findByProfile(ProfileEntity profile);

    // select * from tbl_category where categoriId = ?1 and profileId = ?2
    Optional<CategoryEntity> findByIdAndProfileId(Long id, Long profileId);

    List<CategoryEntity> findByTypeAndProfileId(String type, Long profileId);

    boolean existsByNameAndProfile_Id(String name, Long profileId);

}
