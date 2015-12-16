package ru.trendtech.common.mobileexchange.model.web;

/**
 * Created by petr on 11.02.2015.
 */
public class GlobalDriverStatsInfo {
    private long driverId;
    private String autoModel;
    private int autoYear;
    private String autoClass;
    private String login;
    private String firstName;
    private String lastName;
    private String phone;
    private String taxopark;
    private String assistant;
    private String registrationDate;
    private int balance;
    private boolean ownDriver;
    private int rating; // (10)
    private double percentRatingOnCountOrder; // процент оценок от кол-ва заказов
    private int ratingAVG; // средний бал оценки - ???
    private double percentCommentOnCountOrder; // процент комментов от кол-ва заказов
    private String blockState; //
    private String dateOfBlock;
    private String reasonOfBlock;
    private String dateOfFirstOrder;
    private String dateOfLastOrder;
    private int countMission; // общее количество выполненных заказов
    private double allSummMissionMoney; // Сумма по общему количеству выполненных заказов
    private double averageCheckMoney; // средний чек
    private double averageCountMissionMonth; // среднее кол-во заказов в месяц
    private int countCanceledMissionByClient;
    private int countCanceledMissionByDriver;

    private int averageTimeArriving; //Среднее назначенное времчя подачи
    private int averageTimeArrivingInFact; // Среднее фактическое время подачи

    private double percentPushLateDriver; // % нажатий "опаздываю" на планшете от кол-ва заказов

    private int countRepeatOrderWithClient; //кол-во повторных поездок с клиентом
    private int countOrderWithPromo; //кол-во поездок с промокодом

    //% комметов от кол-ва заказов

    /*
    ID водителя +
    Марка авто +
    год выпуска +
    Класс автомобиля +
    позывной(логин) +
    имя фамилия +
    телефон +
    таксопарк +
    ассистент +
    дата регистрации +
    баланс на текущий момент +
    Текущий тариф ------------------- чем отличается от класса авто???
    Рейтинг ----------------------- стринга? gold или цифра? +
    % оценок от кол-ва заказов +
    Средний бал оценки +
    % комметов от кол-ва заказов +
    статус(блок, не блок) +
    дата блокировки  +
    причина блокировки +
    дата первого заказа +
    дата последнего заказа +
    общее количество выполненных заказов +
    Сумма по общему количеству выполненных заказов +
    Средний чек +
    среднее кол-во заказов в месяц +
    Количество отказов клиентом +
    количество отказов водителем +
    Среднее назначенное времчя подачи +
    Среднее фактическое время подачи +
    % нажатий "опаздываю" на планшете от кол-ва заказов + ???
    кол-во повторных поездок с клиентом + ???
    кол-во поездок с промокодом +
    % комметов от кол-ва заказов ??? - чем отличается от % комметов от кол-ва заказов
    */

    public boolean isOwnDriver() {
        return ownDriver;
    }

    public void setOwnDriver(boolean ownDriver) {
        this.ownDriver = ownDriver;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public String getAutoModel() {
        return autoModel;
    }

    public void setAutoModel(String autoModel) {
        this.autoModel = autoModel;
    }

    public int getAutoYear() {
        return autoYear;
    }

    public void setAutoYear(int autoYear) {
        this.autoYear = autoYear;
    }

    public String getAutoClass() {
        return autoClass;
    }

    public void setAutoClass(String autoClass) {
        this.autoClass = autoClass;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTaxopark() {
        return taxopark;
    }

    public void setTaxopark(String taxopark) {
        this.taxopark = taxopark;
    }

    public String getAssistant() {
        return assistant;
    }

    public void setAssistant(String assistant) {
        this.assistant = assistant;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getPercentRatingOnCountOrder() {
        return percentRatingOnCountOrder;
    }

    public void setPercentRatingOnCountOrder(double percentRatingOnCountOrder) {
        this.percentRatingOnCountOrder = percentRatingOnCountOrder;
    }

    public double getPercentCommentOnCountOrder() {
        return percentCommentOnCountOrder;
    }

    public void setPercentCommentOnCountOrder(double percentCommentOnCountOrder) {
        this.percentCommentOnCountOrder = percentCommentOnCountOrder;
    }

    public double getPercentPushLateDriver() {
        return percentPushLateDriver;
    }

    public void setPercentPushLateDriver(double percentPushLateDriver) {
        this.percentPushLateDriver = percentPushLateDriver;
    }

    public int getRatingAVG() {
        return ratingAVG;
    }

    public void setRatingAVG(int ratingAVG) {
        this.ratingAVG = ratingAVG;
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

    public double getAverageCountMissionMonth() {
        return averageCountMissionMonth;
    }

    public void setAverageCountMissionMonth(double averageCountMissionMonth) {
        this.averageCountMissionMonth = averageCountMissionMonth;
    }

    public int getCountCanceledMissionByClient() {
        return countCanceledMissionByClient;
    }

    public void setCountCanceledMissionByClient(int countCanceledMissionByClient) {
        this.countCanceledMissionByClient = countCanceledMissionByClient;
    }

    public int getCountCanceledMissionByDriver() {
        return countCanceledMissionByDriver;
    }

    public void setCountCanceledMissionByDriver(int countCanceledMissionByDriver) {
        this.countCanceledMissionByDriver = countCanceledMissionByDriver;
    }

    public int getAverageTimeArriving() {
        return averageTimeArriving;
    }

    public void setAverageTimeArriving(int averageTimeArriving) {
        this.averageTimeArriving = averageTimeArriving;
    }

    public int getAverageTimeArrivingInFact() {
        return averageTimeArrivingInFact;
    }

    public void setAverageTimeArrivingInFact(int averageTimeArrivingInFact) {
        this.averageTimeArrivingInFact = averageTimeArrivingInFact;
    }


    public int getCountRepeatOrderWithClient() {
        return countRepeatOrderWithClient;
    }

    public void setCountRepeatOrderWithClient(int countRepeatOrderWithClient) {
        this.countRepeatOrderWithClient = countRepeatOrderWithClient;
    }

    public int getCountOrderWithPromo() {
        return countOrderWithPromo;
    }

    public void setCountOrderWithPromo(int countOrderWithPromo) {
        this.countOrderWithPromo = countOrderWithPromo;
    }
}
