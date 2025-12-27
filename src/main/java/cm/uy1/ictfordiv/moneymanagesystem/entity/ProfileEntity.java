package cm.uy1.ictfordiv.moneymanagesystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_profile")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String fullname;
    @Column(unique=true)
    private  String email;
    private String password;
    private String imageUrl;
    private boolean isActive=false;
    @Column(updatable=false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private String activationToken;

//    @OneToMany
//    private List<CategoryEntity> categories;

    @PrePersist
    public void prePersist() {
        if(this.isActive) {
            this.isActive = true;
        }
    }
}
