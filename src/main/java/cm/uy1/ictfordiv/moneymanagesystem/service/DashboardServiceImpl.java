package cm.uy1.ictfordiv.moneymanagesystem.service;

import cm.uy1.ictfordiv.moneymanagesystem.dto.ExpendDTO;
import cm.uy1.ictfordiv.moneymanagesystem.dto.IncomesDTO;
import cm.uy1.ictfordiv.moneymanagesystem.dto.RecentTrasactionsDTO;
import cm.uy1.ictfordiv.moneymanagesystem.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ExpendService expendService;
    private final IcomesSevice icomesSevice;
    private final ProfileService profileService;

    @Override
    public Map<String, Object> getDashboardData() {
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> dashboardData = new LinkedHashMap<>();

        // Récupérer les données
        List<IncomesDTO> latestIncomes = icomesSevice.get5LastIncomeForCurrentUser();
        List<ExpendDTO> latestExpend = expendService.getTop5LastExpendsForCurrentUser();


        // Combiner et trier les transactions récentes
        //Ce code sert simplement à dire :
        //“Montre-moi tout ce que j’ai fait récemment avec mon argent, dans le bon ordre.”
        List<RecentTrasactionsDTO> recentTransactions = Stream.concat(
                        latestIncomes.stream().map(income -> RecentTrasactionsDTO.builder()
                                .id(income.getId())
                                .name(income.getName())
                                .icon(income.getIcon())
                                .amount(income.getAmount())
                                .date(income.getDate())
                                .updatedAt(income.getUpdatedAt())
                                .createdAt(income.getCreatedAt())
                                .profileId(profile.getId())
                                .type("income")
                                .build()),
                        latestExpend.stream().map(expend -> RecentTrasactionsDTO.builder()
                                .id(expend.getId())
                                .name(expend.getName())
                                .icon(expend.getIcon())
                                .amount(expend.getAmount())
                                .date(expend.getDate())
                                .updatedAt(expend.getUpdatedAt())
                                .createdAt(expend.getCreatedAt())
                                .profileId(profile.getId())
                                .type("expend")
                                .build())

                ).sorted((RecentTrasactionsDTO a,RecentTrasactionsDTO b  )->{
                    // verifi s'il ya deux operation qui on ete enregistreés a la meme date
                    int cmp = b.getDate().compareTo(a.getDate());

                    // si on a des meme date : on compare plutot avec les date de creation ,'il ne sont pas null
                    if (cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                    return cmp;
        }).toList();

               dashboardData.put("totalbalance",icomesSevice.getTotalIncomesForCurrentUser()
                       .subtract(expendService.getTotalExpendsForCurrentUser()));

               dashboardData.put("totalIncomes", icomesSevice.getTotalIncomesForCurrentUser());
               dashboardData.put("totalEpend", expendService.getTotalExpendsForCurrentUser());
               dashboardData.put("recent5incomes", latestIncomes);
               dashboardData.put("recent5expend", latestExpend);
               dashboardData.put("recentTransactions", recentTransactions);

               return dashboardData;
    }
}