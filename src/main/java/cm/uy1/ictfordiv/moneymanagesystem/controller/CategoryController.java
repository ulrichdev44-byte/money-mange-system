package cm.uy1.ictfordiv.moneymanagesystem.controller;

import cm.uy1.ictfordiv.moneymanagesystem.dto.CategoryDTO;
import cm.uy1.ictfordiv.moneymanagesystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    public final CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {

        CategoryDTO savedCategory = categoryService.saveCategory(categoryDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategoriesForCurrentUser() {

        List<CategoryDTO> categoryDTOList = categoryService.getCategoriesForCurrentUser();

        return ResponseEntity.status(HttpStatus.OK).body(categoryDTOList);
    }

    @GetMapping("/{type}")
    public  ResponseEntity<List<CategoryDTO>> getCategoriesByTypeForCurrentUser(@PathVariable String type) {
        List<CategoryDTO> categoryDTOList = categoryService.findByTypeAndProfile(type);
        return ResponseEntity.status(HttpStatus.OK).body(categoryDTOList);

    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory( @RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId) {
        CategoryDTO dto = categoryService.updateCategory(categoryId,categoryDTO);

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

}
