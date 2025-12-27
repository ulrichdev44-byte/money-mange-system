package cm.uy1.ictfordiv.moneymanagesystem.repository;

import cm.uy1.ictfordiv.moneymanagesystem.entity.ExpendEntity;
import cm.uy1.ictfordiv.moneymanagesystem.entity.IncomesEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IcomesRepository extends JpaRepository<IncomesEntity, Long> {
    // select * from tbl_incomes where profileId = ?1 order by date Desc
    List<IncomesEntity> findByProfile_IdOrderByDateDesc(Long profile_id);

    // select * from tbl_incomes where profileId = ?1 order by date Desc limit 5
    List<IncomesEntity> findTop5ByProfile_idOrderByDateDesc(Long profile_id);

    @Query("SELECT SUM(i.amount) from IncomesEntity i where i.profile.id = :profileId")
    BigDecimal findTotalExpendingByProfile_id(@Param("profileId") Long profileId);

    //
    List<IncomesEntity> findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(
            Long profile_id,
            LocalDate start,
            LocalDate end,
            String keyword,
            Sort sort);

    // SELECT *FROM tbl_incomes WHERE profileId = ?1 AND date BETWEEN ?2 AND ?3
    List<IncomesEntity> findByProfile_idAndDateBetween(Long profile_id, LocalDate start, LocalDate end);

    void deleteByProfile_idAndId(Long profile_id, Long id);

    List<IncomesEntity> findByProfile_IdAndDate(Long profile_id, LocalDate date);
}
