package cm.uy1.ictfordiv.moneymanagesystem.service;

public interface NotificationService {

    void SendDailyIncomeExpendReminder();
    void SendDailyExpendSummary();
    void SendDailyIncomesSummary();
}
