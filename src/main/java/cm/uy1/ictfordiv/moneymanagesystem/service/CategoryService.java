package cm.uy1.ictfordiv.moneymanagesystem.service;

import cm.uy1.ictfordiv.moneymanagesystem.dto.CategoryDTO;
import cm.uy1.ictfordiv.moneymanagesystem.dto.ProfileDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CategoryService {

    CategoryDTO saveCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long id,CategoryDTO categoryDTO);

    void deleteCategory(CategoryDTO categoryDTO);

    CategoryDTO getCategoryByIdAndProfilId(Long Categoryid,  Long profilId);

    List<CategoryDTO> getCategoriesForCurrentUser();


    List<CategoryDTO> findByTypeAndProfile(String type);



}
