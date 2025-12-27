package cm.uy1.ictfordiv.moneymanagesystem.service;

import cm.uy1.ictfordiv.moneymanagesystem.dto.IncomesDTO;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IcomesSevice {

    IncomesDTO addIncome(IncomesDTO incomesDTO);
    IncomesDTO updateIncome(IncomesDTO incomesDTO);
    void deleteIncome(Long incomesID);
    List<IncomesDTO> getCurrentMonthIncomeForCurrentUser();
    List<IncomesDTO> get5LastIncomeForCurrentUser();
    BigDecimal getTotalIncomesForCurrentUser();
    IncomesDTO getIncomesById(Long incomesID);
    List<IncomesDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort);
    List<IncomesDTO> findByProfileIdForDate(Long profileId, LocalDate date);
}
