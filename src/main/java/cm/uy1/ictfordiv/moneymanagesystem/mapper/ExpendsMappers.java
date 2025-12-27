package cm.uy1.ictfordiv.moneymanagesystem.mapper;

import cm.uy1.ictfordiv.moneymanagesystem.dto.ExpendDTO;
import cm.uy1.ictfordiv.moneymanagesystem.entity.CategoryEntity;
import cm.uy1.ictfordiv.moneymanagesystem.entity.ExpendEntity;
import cm.uy1.ictfordiv.moneymanagesystem.entity.ProfileEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@Data
public class ExpendsMappers {

    public ExpendEntity toExpendEntity(ExpendDTO expendDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity){
        ExpendEntity expendEntity = new ExpendEntity();

        expendEntity.setProfile(profileEntity);
        expendEntity.setCategory(categoryEntity);
        expendEntity.setName(expendDTO.getName());
        expendEntity.setAmount(expendDTO.getAmount());
        expendEntity.setIcon(expendDTO.getIcon());
        expendEntity.setDate(expendDTO.getDate());

        return expendEntity;
    }

    public ExpendDTO toExpendDTO(ExpendEntity expendEntity){

        return ExpendDTO.builder()
                .Id(expendEntity.getId())
                .Name(expendEntity.getName())
                .icon(expendEntity.getIcon())
                .categoryId(expendEntity.getCategory() != null ? expendEntity.getCategory().getId() : null)
                .categoryName(expendEntity.getCategory() != null ? expendEntity.getCategory().getName() : null)
                .amount(expendEntity.getAmount())
                .date(expendEntity.getDate())
                .createdAt(expendEntity.getCreatedAt())
                .updatedAt(expendEntity.getUpdatedAt())
                .build();
    }
}
