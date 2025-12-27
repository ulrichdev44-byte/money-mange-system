package cm.uy1.ictfordiv.moneymanagesystem.service;

import cm.uy1.ictfordiv.moneymanagesystem.dto.IncomesDTO;
import cm.uy1.ictfordiv.moneymanagesystem.entity.CategoryEntity;
import cm.uy1.ictfordiv.moneymanagesystem.entity.IncomesEntity;
import cm.uy1.ictfordiv.moneymanagesystem.entity.ProfileEntity;
import cm.uy1.ictfordiv.moneymanagesystem.mapper.IcomesMappers;
import cm.uy1.ictfordiv.moneymanagesystem.repository.CategoryRepository;
import cm.uy1.ictfordiv.moneymanagesystem.repository.IcomesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IcomesSeviceImpl implements IcomesSevice {

    private final ProfileService profileService;
    private final IcomesMappers icomesMappers;
    private final IcomesRepository icomesRepository;
    private final CategoryRepository icategoryRepository;
    /**
     * @param incomesDTO
     * @return
     */
    @Override
    public IncomesDTO addIncome(IncomesDTO incomesDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = icategoryRepository.findById(incomesDTO.getCategoryId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found! "));

        IncomesEntity newIncoms = icomesMappers.toIncomesEntity(incomesDTO, profile, category);
        IncomesEntity savedIcomes = icomesRepository.save(newIncoms);
        System.out.println("saved incomeses is : "+savedIcomes);
        return icomesMappers.toIncomesDto(savedIcomes);
    }

    /**
     * @param incomesDTO
     * @return
     */
    @Override
    public IncomesDTO updateIncome(IncomesDTO incomesDTO) {

        ProfileEntity profile = profileService.getCurrentProfile();

        IncomesEntity incomesEntity = icomesRepository.findById(incomesDTO.getId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found! "));
        incomesEntity.setName(incomesDTO.getName());
        incomesEntity.setAmount(incomesDTO.getAmount());
        incomesEntity.setDate(incomesDTO.getDate());
        incomesEntity.setCategory(icategoryRepository.findById(incomesDTO.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found! "))
        );
        incomesEntity.setIcon(incomesDTO.getIcon());

        return icomesMappers.toIncomesDto(icomesRepository.save(incomesEntity)) ;
    }

    /**
     * @param incomesId
     * @return void
     */
    @Override
    public void deleteIncome(Long incomesId) {

        ProfileEntity profile = profileService.getCurrentProfile();

        IncomesEntity existingIncoms = icomesRepository.findById(incomesId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Incomes not found! "));

        if (!existingIncoms.getProfile().getId().equals(profile.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Incomes already deleted!");
        }
        icomesRepository.delete(existingIncoms);

         //icomesRepository.deleteByProfile_idAndId(profile.getId(), incomesId);
    }

    /**
     */
    @Override
    public List<IncomesDTO> getCurrentMonthIncomeForCurrentUser() {

        ProfileEntity p = profileService.getCurrentProfile();
        LocalDate today = LocalDate.now();
        LocalDate start = today.withDayOfMonth(1); // fixe la date au premier jour du mois
        LocalDate end = today.withDayOfMonth(today.lengthOfMonth());

        List<IncomesEntity> incomesEntityList = icomesRepository.findByProfile_idAndDateBetween(p.getId(), start, end);

        return  incomesEntityList.stream()
                .map(icomesMappers::toIncomesDto).toList();


    }

    /**
     * @return
     */
    @Override
    public List<IncomesDTO> get5LastIncomeForCurrentUser() {
        ProfileEntity p = profileService.getCurrentProfile();
        return  (icomesRepository.findTop5ByProfile_idOrderByDateDesc(p.getId()))
                .stream()
                .map(icomesMappers::toIncomesDto)
                .toList();
    }

    /**
     *
     * @return
     */
    @Override
    public BigDecimal getTotalIncomesForCurrentUser() {
        ProfileEntity p = profileService.getCurrentProfile();

        BigDecimal total = icomesRepository.findTotalExpendingByProfile_id(p.getId());
        return total == null ? BigDecimal.ZERO : total;
    }

    /**
     * @param incomesID
     * @return
     */
    @Override
    public IncomesDTO getIncomesById(Long incomesID) {
        IncomesEntity entity = icomesRepository.getReferenceById(incomesID);
        return icomesMappers.toIncomesDto(entity);
    }

    /**
     * @param startDate
     * @param endDate
     * @param keyword
     * @param sort
     * @return
     */
    @Override
    public List<IncomesDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomesEntity> list=  icomesRepository.findByProfile_IdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);
        return list.stream().map(icomesMappers::toIncomesDto).toList();
    }

    @Override
    public List<IncomesDTO> findByProfileIdForDate(Long profileId, LocalDate date) {
                List<IncomesEntity> incomesEntityList = icomesRepository.findByProfile_IdAndDate(profileId, date);
        return incomesEntityList.stream()
                .map(icomesMappers::toIncomesDto).toList();
    }


}
