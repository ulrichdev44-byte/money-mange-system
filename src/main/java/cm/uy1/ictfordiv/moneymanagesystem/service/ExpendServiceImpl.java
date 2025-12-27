package cm.uy1.ictfordiv.moneymanagesystem.service;

import cm.uy1.ictfordiv.moneymanagesystem.dto.ExpendDTO;
import cm.uy1.ictfordiv.moneymanagesystem.entity.CategoryEntity;
import cm.uy1.ictfordiv.moneymanagesystem.entity.ExpendEntity;
import cm.uy1.ictfordiv.moneymanagesystem.entity.ProfileEntity;
import cm.uy1.ictfordiv.moneymanagesystem.mapper.ExpendsMappers;
import cm.uy1.ictfordiv.moneymanagesystem.repository.CategoryRepository;
import cm.uy1.ictfordiv.moneymanagesystem.repository.ExpendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExpendServiceImpl implements ExpendService {

    private final ExpendRepository expendRepository;
    private final ProfileService profileService;
    private final ExpendsMappers expendsMappers;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    /**
     * @param expend
     * @return
     */
    @Override
    public ExpendDTO addExpend(ExpendDTO expend) {

        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(expend.getCategoryId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found! "));
        ExpendEntity newExpend = expendsMappers.toExpendEntity(expend, profile, category);

        expendRepository.save(newExpend);
        return expendsMappers.toExpendDTO(expendRepository
                .save(newExpend));
    }

    @Override
    public List<ExpendDTO>getCurrentMonthExpendsForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());

        List<ExpendEntity> expends = expendRepository.findByProfile_idAndDateBetween(profile.getId(),startDate, endDate);

        return expends.stream().map(expendsMappers::toExpendDTO).toList();
    }

    /**
     * @return
     */
    @Override
    public List<ExpendDTO> getTop5LastExpendsForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();

        List<ExpendEntity> expend = expendRepository.findTop5ByProfile_idOrderByDateDesc(profile.getId());
        return expend.stream().map(expendsMappers::toExpendDTO).toList();
    }

    /**
     * @param expend
     * @return
     */
    @Override
    public ExpendDTO updateExpend(ExpendDTO expend) {
        return null;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ExpendDTO getExpend(int id) {
        return null;
    }




    /**
     * @param id
     */
    @Override
    public void deleteExpend(Long id) {
     ProfileEntity profile = profileService.getCurrentProfile();

     ExpendEntity expend = expendRepository.findById(id)
             .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Expend not found! "));

     if (!expend.getProfile().getId().equals(profile.getId())) {
         throw new ResponseStatusException(HttpStatus.CONFLICT,"Expend already exists!");
     }
         expendRepository.delete(expend);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public BigDecimal getTotalExpendsForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = (BigDecimal)expendRepository.findTotalExpendingByProfile_id(profile.getId());

        return total == null ? BigDecimal.ZERO : total;
    }

    /**
     * @param startDate
     * @param endDate
     * @param keyword
     * @param sort
     * @return
     */
    @Override
    public List<ExpendDTO> filterExpend(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpendEntity> list = expendRepository.findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(),startDate,endDate, keyword, sort);
        return list.stream().map(expendsMappers::toExpendDTO).toList();
    }

    /**
     * @param profileID
     * @param date
     * @return
     */
    @Override
    public List<ExpendDTO> findByProfileIdAndDate(Long profileID, LocalDate date) {
       List<ExpendEntity> list = expendRepository.findByProfile_IdAndDate(profileID, date);
        return list.stream().map((expendsMappers::toExpendDTO)).toList();
    }
}
