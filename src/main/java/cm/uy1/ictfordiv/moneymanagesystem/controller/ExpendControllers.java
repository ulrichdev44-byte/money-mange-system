package cm.uy1.ictfordiv.moneymanagesystem.controller;

import cm.uy1.ictfordiv.moneymanagesystem.dto.ExpendDTO;
import cm.uy1.ictfordiv.moneymanagesystem.service.ExpendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpendControllers {

    private final ExpendService expendService;

    @PostMapping("/creat")
    public ResponseEntity<ExpendDTO> addExpend(@RequestBody ExpendDTO expendDTO) {
        expendService.addExpend(expendDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(expendDTO);

    }
//    public ResponseEntity<ExpendDTO> updateExpend(@RequestBody ExpendDTO expendDTO) {}

    @GetMapping
    public ResponseEntity<List<ExpendDTO>> getAllExpend() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(expendService.getCurrentMonthExpendsForCurrentUser());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpend( @PathVariable Long id) {
        expendService.deleteExpend(id);
        return  ResponseEntity.noContent().build();
    }

    // get

}
