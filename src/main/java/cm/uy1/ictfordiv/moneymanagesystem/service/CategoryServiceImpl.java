package cm.uy1.ictfordiv.moneymanagesystem.service;

import cm.uy1.ictfordiv.moneymanagesystem.dto.CategoryDTO;
import cm.uy1.ictfordiv.moneymanagesystem.entity.CategoryEntity;
import cm.uy1.ictfordiv.moneymanagesystem.entity.ProfileEntity;
import cm.uy1.ictfordiv.moneymanagesystem.mapper.CategoryMappers;
import cm.uy1.ictfordiv.moneymanagesystem.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;
    private final CategoryMappers categoryMappers;
    /**
     * @param categoryDTO
     * @return
     */
    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {

        Long proFileId = profileService.getCurrentProfile().getId();

        if (categoryRepository.existsByNameAndProfile_Id(categoryDTO.getName(),proFileId))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category name already exists");
        }

        CategoryEntity newCategory = categoryRepository.save(
                categoryMappers.toCategoryEntity(categoryDTO));

        newCategory.setProfile(profileService.getCurrentProfile());

        return categoryMappers
                .toCategoryDTO(newCategory);
    }

    /**
     * @param categoryDTO
     * @return
     */
    @Override
    public CategoryDTO updateCategory(Long id ,CategoryDTO categoryDTO) {
        ProfileEntity profile = profileService.getCurrentProfile();

        CategoryEntity existingCategory = categoryRepository.findByIdAndProfileId(id, profile.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found")
        );

        existingCategory.setName(categoryDTO.getName());
        existingCategory.setDescription(categoryDTO.getDescription());
        existingCategory.setType(categoryDTO.getType());
        existingCategory.setIcon(categoryDTO.getIcon());


     CategoryEntity updatedCategoris = categoryRepository
             .save(existingCategory);

        System.out.println("Updated: "+updatedCategoris.toString());

        return categoryMappers.toCategoryDTO(updatedCategoris);
    }

    /**
     * @param categoryDTO
     */
    @Override
    public void deleteCategory(CategoryDTO categoryDTO) {

    }

    /**
     * @param Categoryid
     * @param profilId
     * @return
     */
    @Override
    public CategoryDTO getCategoryByIdAndProfilId(Long Categoryid, Long profilId) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public List<CategoryDTO> getCategoriesForCurrentUser() {
        ProfileEntity currentProfile = profileService.getCurrentProfile();

        List<CategoryEntity> categoryList = categoryRepository.findByProfile(currentProfile);
        return categoryList.stream().map(categoryMappers::toCategoryDTO).toList();
    }

    /**
     * @param type
     * @return List<ProfileDTO>
     * @description get categorie by type for a current user
     */
    @Override
    public List<CategoryDTO> findByTypeAndProfile(String type) {

        ProfileEntity currentProfile = profileService.getCurrentProfile();
        List<CategoryEntity> categoryList = categoryRepository.findByTypeAndProfileId(type,currentProfile.getId());

        return categoryList.stream()
                .map(categoryMappers::toCategoryDTO)
                .toList();
    }
}
