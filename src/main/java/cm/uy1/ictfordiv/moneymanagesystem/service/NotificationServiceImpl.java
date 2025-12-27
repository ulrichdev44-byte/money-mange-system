package cm.uy1.ictfordiv.moneymanagesystem.service;

import cm.uy1.ictfordiv.moneymanagesystem.dto.ExpendDTO;
import cm.uy1.ictfordiv.moneymanagesystem.dto.IncomesDTO;
import cm.uy1.ictfordiv.moneymanagesystem.dto.ProfileDTO;
import cm.uy1.ictfordiv.moneymanagesystem.entity.ProfileEntity;
import cm.uy1.ictfordiv.moneymanagesystem.repository.ProfileRepository;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private  final SendMailService sendMailService;
    private final ExpendService expendService;
    private final IcomesSevice icomesSevice;

    private final ProfileRepository profileRepository;

    @Value("${money.manager.frontend.url}")
    private String frontendUrl; // url du frontend
    /**
     *
     */
    @Override
    /*
    *🔹 Définition
@Scheduled est une annotation Spring qui permet d’exécuter automatiquement
* une méthode à intervalles réguliers,
* sans appel HTTP, sans utilisateur, sans contrôleur.
On parle de tâches planifiées (Scheduled Jobs).
* envoyer des emails automatiques
il sert a :exécuter des nettoyages de base de données,générer des rapports périodiques,
synchroniser des données ,envoyer des notifications ,effectuer des calculs quotidiens
    *
    * */
    @Scheduled(cron = "0 * * * * *", zone = "IST")
    //@Scheduled(cron = "0 0 10 * * *", zone = "IST") // permet de definir la frenquence d'envoi d'email par jour
    public void SendDailyIncomeExpendReminder() {

        log.info("Job started: SendDailyIncomeExpendReminder");

        List<ProfileEntity> profiles = profileRepository.findAll();

        // we go through all the profiles which are in dababase and send mail for each profile

        for (ProfileEntity profile : profiles) {
            String body = "Hi"+profile.getFullname()+", <br><br>" +
                    "This is friendly remaind you all expend and Incomes for today in money" +
                    "click on thi link for consult about that" +
                    "<a href="+frontendUrl+" style=''>"+profile.getFullname()+"</a>" +
                    "<br><br> Best regard money team";
                   // sendMailService.sendMail(profile.getEmail(), "Daily remainder add and expend money",body);

            System.out.println("********************* Mail had been sent to "+profile.getFullname()+"he can check out his email inbox ****************");

        }

    }


    @Override
   // @Scheduled(cron = "0 0 23 * * *",zone = "IST")
    @Scheduled(cron = "0 * * * * *", zone = "IST")
    public void SendDailyExpendSummary() {
        log.info("Job Complete : SendDailyExpendSummary");

        // get all profiles
        List<ProfileEntity> profiles = profileRepository.findAll();

        for (ProfileEntity profile : profiles) {
            // get all today expend for each user on date


            List<ExpendDTO> todayExpend = expendService.findByProfileIdAndDate(profile.getId(), LocalDate.now());
            todayExpend.forEach(System.out::println);
            if (!todayExpend.isEmpty()){ // if todayExspend = false
                // generate HtML page for send email message to user profile

                StringBuilder table = new StringBuilder();
                table.append("<table> border=1 style='border-collapse:collapse; width:100%;'>'");
                table.append("<tr style='background-color:#F0F0F0;'>" +
                        "<th style='border:1px solid #F0F0F0;'><td>N0</td><td>Name</td><td>Amount</td><td>Created date</td></th></tr>");
                int i = 0;

                for (ExpendDTO expends: todayExpend){
                    table.append("<tr>");
                    table.append("<td style='border: 1 solid #add; padding:8px;'>").append(i++).append("</td>");
                    table.append("<td style='border: 1 solid #add; padding:8px;'>").append(expends.getName()).append("</td>");
                    table.append("<td style='border: 1 solid #add; padding:8px;'>").append(expends.getAmount()).append("</td>");
                    table.append("<td style='border: 1 solid #add; padding:8px;'>").append(expends.getCreatedAt()).append("</td>");
                    table.append("</tr>");
                }
                table.append("</table>");

                String body = "Hi"+profile.getFullname()+" Here id you summary of your expends </br></br> "+table;
                sendMailService.sendMail(profile.getEmail(), "Your total summary expend for today", body);
                System.out.println("*************************** Summary incomes for user : "+profile.getFullname()+" ****************************");

            }
        }
    }

    @Scheduled(cron = "0 * * * * *", zone = "IST")
    @Override
    public void SendDailyIncomesSummary() {

        log.info("Job Complete : SendDailyIncomesSummary");
        List<ProfileEntity> profiles = profileRepository.findAll();
        System.out.println(profiles);

        for (ProfileEntity profile : profiles){

            List<IncomesDTO> incomesList = icomesSevice.findByProfileIdForDate(profile.getId(),LocalDate.now());
            incomesList.forEach(System.out::println);
            if(!incomesList.isEmpty()){

                StringBuilder table = new StringBuilder();
                table.append("<table> border=1 style='border-collapse:collapse; width:100%;'>");
                table.append("<tr style='background-color:#F0F0F0;'>" +
                        "<th style='border:1px solid #F0F0F0;'><td>N0</td><td>Name</td><td>Amount</td><td>Created date</td></th></tr>");

                int i = 0;

                for (IncomesDTO incomes : incomesList){
                    table.append("<tr>");
                    table.append("<td style='border: 1 solid #add; padding:8px;'>").append(i++).append("</td>");
                    table.append("<td style='border: 1 solid #add; padding:8px;'>").append(incomes.getName()).append("</td>");
                    table.append("<td style='border: 1 solid #add; padding:8px;'>").append(incomes.getAmount()).append("</td>");
                    table.append("<td style='border: 1 solid #add; padding:8px;'>").append(incomes.getCreatedAt()).append("</td>");
                    table.append("</tr>");
                }
                table.append("</table>");

                String body = "Hi"+profile.getFullname()+" Here id you summary of your expends </br></br> "+table;
                sendMailService.sendMail(profile.getEmail(), "Your total summary expend for today", body);
                System.out.println("*************************** Summary incomes for user : "+profile.getFullname()+" ****************************");

            }

        }

    }

}
