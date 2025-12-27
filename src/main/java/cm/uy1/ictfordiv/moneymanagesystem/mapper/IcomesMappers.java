package cm.uy1.ictfordiv.moneymanagesystem.mapper;

import cm.uy1.ictfordiv.moneymanagesystem.dto.IncomesDTO;
import cm.uy1.ictfordiv.moneymanagesystem.entity.CategoryEntity;
import cm.uy1.ictfordiv.moneymanagesystem.entity.IncomesEntity;
import cm.uy1.ictfordiv.moneymanagesystem.entity.ProfileEntity;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class IcomesMappers {

    public IncomesDTO toIncomesDto(IncomesEntity incomesEntity) {

        return IncomesDTO.builder()
                .Id(incomesEntity.getId())
                .Name(incomesEntity.getName())
                .icon(incomesEntity.getIcon())
                .categoryId(incomesEntity.getCategory() != null ?incomesEntity.getCategory().getId() : null)
                .categoryName(incomesEntity.getCategory() != null ? incomesEntity.getCategory().getName() : null)
                .amount(incomesEntity.getAmount())
                .date(incomesEntity.getDate())
                .createdAt(incomesEntity.getCreatedAt())
                .build();
    }

    public IncomesEntity toIncomesEntity(IncomesDTO incomesDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity) {
        return IncomesEntity.builder()
                .name(incomesDTO.getName())
                .icon(incomesDTO.getIcon())
                .amount(incomesDTO.getAmount())
                .category(categoryEntity)
                .date(incomesDTO.getDate())
                .profile(profileEntity)
                .build();
    }

}
