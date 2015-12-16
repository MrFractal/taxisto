package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 12.02.2015.
 */
public class GlobalClientStatsInfo {
    private long clientId;
    private String firstName;
    private String LastName;
    private String registrationDate;
    private String city;
    private String motive;
    private String blockState;
    private String dateOfBlock;
    private String reasonOfBlock;
    private String dateOfFirstOrder;
    private String dateOfLastOrder;
    private int countMission;
    private double averageCountMissionMonth;
    private double allSummMissionMoney;
    private double averageCheckMoney;
    private int countLikeMission;
    private int countEqualMission;
    private int countCanceledMissionWithoutDriver;
    private int countCanceledMissionWithDriver;
    private int countLowCoster;
    private int countStandard;
    private int countComfort;
    private int countBusiness;
    private int countEstimate;
    private int avgRate; // средний бал
    private int countComments;
    private int countPromoSend;
    private int countPromoActivated;
    private int countCallInSupport;
    private int countAutosearch;
    private int countServices;
    private int countWatchMission;

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMotive() {
        return motive;
    }

    public void setMotive(String motive) {
        this.motive = motive;
    }

    public String getBlockState() {
        return blockState;
    }

    public void setBlockState(String blockState) {
        this.blockState = blockState;
    }

    public String getDateOfBlock() {
        return dateOfBlock;
    }

    public void setDateOfBlock(String dateOfBlock) {
        this.dateOfBlock = dateOfBlock;
    }

    public String getReasonOfBlock() {
        return reasonOfBlock;
    }

    public void setReasonOfBlock(String reasonOfBlock) {
        this.reasonOfBlock = reasonOfBlock;
    }

    public String getDateOfFirstOrder() {
        return dateOfFirstOrder;
    }

    public void setDateOfFirstOrder(String dateOfFirstOrder) {
        this.dateOfFirstOrder = dateOfFirstOrder;
    }

    public String getDateOfLastOrder() {
        return dateOfLastOrder;
    }

    public void setDateOfLastOrder(String dateOfLastOrder) {
        this.dateOfLastOrder = dateOfLastOrder;
    }

    public int getCountMission() {
        return countMission;
    }

    public void setCountMission(int countMission) {
        this.countMission = countMission;
    }

    public double getAverageCountMissionMonth() {
        return averageCountMissionMonth;
    }

    public void setAverageCountMissionMonth(double averageCountMissionMonth) {
        this.averageCountMissionMonth = averageCountMissionMonth;
    }

    public double getAllSummMissionMoney() {
        return allSummMissionMoney;
    }

    public void setAllSummMissionMoney(double allSummMissionMoney) {
        this.allSummMissionMoney = allSummMissionMoney;
    }

    public double getAverageCheckMoney() {
        return averageCheckMoney;
    }

    public void setAverageCheckMoney(double averageCheckMoney) {
        this.averageCheckMoney = averageCheckMoney;
    }

    public int getCountLikeMission() {
        return countLikeMission;
    }

    public void setCountLikeMission(int countLikeMission) {
        this.countLikeMission = countLikeMission;
    }

    public int getCountEqualMission() {
        return countEqualMission;
    }

    public void setCountEqualMission(int countEqualMission) {
        this.countEqualMission = countEqualMission;
    }

    public int getCountCanceledMissionWithoutDriver() {
        return countCanceledMissionWithoutDriver;
    }

    public void setCountCanceledMissionWithoutDriver(int countCanceledMissionWithoutDriver) {
        this.countCanceledMissionWithoutDriver = countCanceledMissionWithoutDriver;
    }

    public int getCountCanceledMissionWithDriver() {
        return countCanceledMissionWithDriver;
    }

    public void setCountCanceledMissionWithDriver(int countCanceledMissionWithDriver) {
        this.countCanceledMissionWithDriver = countCanceledMissionWithDriver;
    }

    public int getCountLowCoster() {
        return countLowCoster;
    }

    public void setCountLowCoster(int countLowCoster) {
        this.countLowCoster = countLowCoster;
    }

    public int getCountStandard() {
        return countStandard;
    }

    public void setCountStandard(int countStandard) {
        this.countStandard = countStandard;
    }

    public int getCountComfort() {
        return countComfort;
    }

    public void setCountComfort(int countComfort) {
        this.countComfort = countComfort;
    }

    public int getCountBusiness() {
        return countBusiness;
    }

    public void setCountBusiness(int countBusiness) {
        this.countBusiness = countBusiness;
    }

    public int getCountEstimate() {
        return countEstimate;
    }

    public void setCountEstimate(int countEstimate) {
        this.countEstimate = countEstimate;
    }

    public int getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(int avgRate) {
        this.avgRate = avgRate;
    }

    public int getCountComments() {
        return countComments;
    }

    public void setCountComments(int countComments) {
        this.countComments = countComments;
    }

    public int getCountPromoSend() {
        return countPromoSend;
    }

    public void setCountPromoSend(int countPromoSend) {
        this.countPromoSend = countPromoSend;
    }

    public int getCountPromoActivated() {
        return countPromoActivated;
    }

    public void setCountPromoActivated(int countPromoActivated) {
        this.countPromoActivated = countPromoActivated;
    }

    public int getCountCallInSupport() {
        return countCallInSupport;
    }

    public void setCountCallInSupport(int countCallInSupport) {
        this.countCallInSupport = countCallInSupport;
    }

    public int getCountAutosearch() {
        return countAutosearch;
    }

    public void setCountAutosearch(int countAutosearch) {
        this.countAutosearch = countAutosearch;
    }

    public int getCountServices() {
        return countServices;
    }

    public void setCountServices(int countServices) {
        this.countServices = countServices;
    }

    public int getCountWatchMission() {
        return countWatchMission;
    }

    public void setCountWatchMission(int countWatchMission) {
        this.countWatchMission = countWatchMission;
    }

    /*
    ID клиента +
    Имя фамилия +
    Дата регистрации +
    Город +
    Мотив +
    статус +
    дата блокировки +
    причина блокировки +
    Дата первого заказ +
    Дата последнего заказа +
    Общее количество заказов +
    Среднее кол-во заказов в месяц +
    Сумма оплаченная за все заказы +
    Средний чек +
    Количество однотипных заказов (совпадения адресов подачи или назначения) +
    Количество одинаковых заказов (полное совпадение адресов подачи ,назначения и направления) +
    Количество отказов без назначения водителя общее +
    Количество отказов при назначенном водителе общее +
    Количество Лоукостер +
    Количество Стандарт +
    Количество Комфорт +
    Количество Бизнес +
    кол-во оценок +
    средний бал +
    кол-во комментов +
    отправил другим +
    активировано другими +
    Кол-во звонков в поддержку из приложения +
    Кол-во раз использования дополнительных функций (Бинокль	Autosearch	Доп.услуги) +
        */


}
