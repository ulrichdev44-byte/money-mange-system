package cm.uy1.ictfordiv.moneymanagesystem.controller;

import cm.uy1.ictfordiv.moneymanagesystem.dto.IncomesDTO;
import cm.uy1.ictfordiv.moneymanagesystem.service.IcomesSevice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomeses")
public class IncomesController {
    private final IcomesSevice icomesSevice;

    @PostMapping("/add")
    public ResponseEntity<IncomesDTO> addIncomes(@RequestBody IncomesDTO incomesDTO){
        IncomesDTO saveedIncomes = icomesSevice.addIncome(incomesDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(saveedIncomes);
    }

    @PutMapping("/update")
    public  ResponseEntity<IncomesDTO> upadteIncomes(@RequestBody IncomesDTO incomesDTO){
        IncomesDTO upadtedIncomes = icomesSevice.updateIncome(incomesDTO);
        return ResponseEntity.status(HttpStatus.OK).body(upadtedIncomes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncomes(@PathVariable Long id){
      icomesSevice.deleteIncome(id);
    return  ResponseEntity.noContent().build();
    }

    @GetMapping("/incomes")
    public ResponseEntity<List<IncomesDTO>> getAllIncomes(){
        return ResponseEntity.status(HttpStatus.OK).body(icomesSevice.
                getCurrentMonthIncomeForCurrentUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomesDTO> getIncomesById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(icomesSevice.getIncomesById(id));
    }

}
