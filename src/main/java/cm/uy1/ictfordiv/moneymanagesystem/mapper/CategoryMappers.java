package cm.uy1.ictfordiv.moneymanagesystem.mapper;

import cm.uy1.ictfordiv.moneymanagesystem.dto.CategoryDTO;
import cm.uy1.ictfordiv.moneymanagesystem.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Data
@Service
public class CategoryMappers {
    public CategoryEntity toCategoryEntity(CategoryDTO categoryDTO){
        CategoryEntity categoryEntity = new CategoryEntity();

        BeanUtils.copyProperties(categoryDTO, categoryEntity);
        return categoryEntity;
    }

    public CategoryDTO toCategoryDTO(CategoryEntity categoryEntity){
        CategoryDTO categoryDTO = new CategoryDTO();
        BeanUtils.copyProperties(categoryEntity, categoryDTO);
        return categoryDTO;
    }
}
