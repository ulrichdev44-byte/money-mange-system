package cm.uy1.ictfordiv.moneymanagesystem.controller;

import cm.uy1.ictfordiv.moneymanagesystem.dto.ExpendDTO;
import cm.uy1.ictfordiv.moneymanagesystem.dto.FilterDTO;
import cm.uy1.ictfordiv.moneymanagesystem.dto.IncomesDTO;
import cm.uy1.ictfordiv.moneymanagesystem.service.ExpendService;
import cm.uy1.ictfordiv.moneymanagesystem.service.IcomesSevice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
public class FilterController {

    private final ExpendService expendService;
    private final IcomesSevice icomesSevice;

    @PostMapping
    public ResponseEntity<?> filterTrqnsqctions(@RequestBody FilterDTO filterDTO){

        LocalDate startDate = filterDTO.getStartDate() != null ? filterDTO.getStartDate(): LocalDate.now();
        LocalDate endDate = filterDTO.getEndDate() != null ? filterDTO.getEndDate(): LocalDate.now();
        String keyword = filterDTO.getKeyword() != null ? filterDTO.getKeyword(): "";
        String sortField = filterDTO.getSortField() != null ? filterDTO.getSortField(): "date";

        Sort.Direction direction ="desc".equalsIgnoreCase(filterDTO.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);
        // verification du type de tri choisi
        if ("income".equals(filterDTO.getType())) {
            List<IncomesDTO> incomesDTOList = icomesSevice.filterIncomes(startDate,endDate,keyword,sort);

            return ResponseEntity.ok(incomesDTOList);
        }else if ("expend".equals(filterDTO.getType())) {
            List<ExpendDTO> expendDTOList = expendService.filterExpend(startDate,endDate,keyword,sort);
            return ResponseEntity.ok(expendDTOList);
        }
        return ResponseEntity.badRequest().body("Invalid type must be 'icomes' or 'expend'");
    }
}
