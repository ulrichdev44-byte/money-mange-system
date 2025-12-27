package cm.uy1.ictfordiv.moneymanagesystem.service;

import cm.uy1.ictfordiv.moneymanagesystem.dto.ExpendDTO;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpendService {

    ExpendDTO addExpend(ExpendDTO expend);

    List<ExpendDTO>getCurrentMonthExpendsForCurrentUser();
    List<ExpendDTO> getTop5LastExpendsForCurrentUser();
    ExpendDTO updateExpend(ExpendDTO expend);
    ExpendDTO getExpend(int id);
    void deleteExpend(Long id);
    BigDecimal getTotalExpendsForCurrentUser();
    List<ExpendDTO> filterExpend(LocalDate startDate, LocalDate endDate, String keyword, Sort sort);
    List<ExpendDTO> findByProfileIdAndDate(Long profileID, LocalDate date);







}
