package cm.uy1.ictfordiv.moneymanagesystem.repository;

import cm.uy1.ictfordiv.moneymanagesystem.dto.ExpendDTO;
import cm.uy1.ictfordiv.moneymanagesystem.entity.ExpendEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ExpendRepository extends JpaRepository<ExpendEntity, Long> {

    // select * from tbl_expend where profileId = ?1 order by date Desc
    List<ExpendEntity> findByProfile_IdOrderByDateDesc(Long profile_id);

    // select * from tbl_expend where profileId = ?1 order by date Desc limit 5
    List<ExpendEntity> findTop5ByProfile_idOrderByDateDesc(Long profile_id);

    @Query("SELECT SUM(e.amount) from ExpendEntity e where e.profile.id = :profileId")
    BigDecimal findTotalExpendingByProfile_id(@Param("profileId") Long profileId);

    //
    List<ExpendEntity> findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(
            Long profile_id,
            LocalDate start,
            LocalDate end,
            String keyword,
            Sort sort);

    // SELECT *FROM tbl_expend WHERE profileId = ?1 AND date BETWEEN ?2 AND ?3
        List<ExpendEntity> findByProfile_idAndDateBetween(Long profile_id, LocalDate start, LocalDate end);

        List<ExpendEntity> findByProfile_IdAndDate(Long profile_id, LocalDate date);
}
