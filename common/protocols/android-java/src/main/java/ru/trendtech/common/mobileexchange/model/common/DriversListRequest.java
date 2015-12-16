package ru.trendtech.common.mobileexchange.model.common;

import java.util.List;

/**
 * Created by petr on 08.08.14.
 */
public class DriversListRequest {
    private long driverId;
    private int numberPage;
    private int sizePage;
    private String nameMask;
    private String phoneMask;
    private String carModelMask; // марка авто или часть
    private String carNumberMask; //гос номер авто или часть }
    private long assistantId;
    private String versionApp;
    private String login;
    private String autoClass;
    private String blockStaus;
    private String security_token;
    private Integer typeSalary; // 0 - зарплатник, 1 - на %
    private Integer salaryPriority; // 0 - в штате, 1 - не в штате
    private Boolean typeX;
    private Boolean active;
    private Boolean courier;
    private Boolean pedestrian;
    private List<String> driverTypes; // DRIVER, COURIER, BOTH

    public List<String> getDriverTypes() {
        return driverTypes;
    }

    public void setDriverTypes(List<String> driverTypes) {
        this.driverTypes = driverTypes;
    }

    public Boolean getCourier() {
        return courier;
    }

    public void setCourier(Boolean courier) {
        this.courier = courier;
    }

    public Boolean getPedestrian() {
        return pedestrian;
    }

    public void setPedestrian(Boolean pedestrian) {
        this.pedestrian = pedestrian;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isTypeX() {
        return typeX;
    }

    public void setTypeX(Boolean typeX) {
        this.typeX = typeX;
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public Integer getTypeSalary() {
        return typeSalary;
    }

    public void setTypeSalary(Integer typeSalary) {
        this.typeSalary = typeSalary;
    }

    public Integer getSalaryPriority() {
        return salaryPriority;
    }

    public void setSalaryPriority(Integer salaryPriority) {
        this.salaryPriority = salaryPriority;
    }

    public String getSecurity_token() {
        return security_token;
    }

    public void setSecurity_token(String security_token) {
        this.security_token = security_token;
    }

    public String getVersionApp() {
        return versionApp;
    }

    public void setVersionApp(String versionApp) {
        this.versionApp = versionApp;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAutoClass() {
        return autoClass;
    }

    public void setAutoClass(String autoClass) {
        this.autoClass = autoClass;
    }

    public String getBlockStaus() {
        return blockStaus;
    }

    public void setBlockStaus(String blockStaus) {
        this.blockStaus = blockStaus;
    }

    public long getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(long assistantId) {
        this.assistantId = assistantId;
    }

    public int getNumberPage() {
        return numberPage;
    }

    public void setNumberPage(int numberPage) {
        this.numberPage = numberPage;
    }

    public int getSizePage() {
        return sizePage;
    }

    public void setSizePage(int sizePage) {
        this.sizePage = sizePage;
    }

//    public int getCountItems() {
//        return countItems;
//    }
//
//    public void setCountItems(int countItems) {
//        this.countItems = countItems;
//    }
//
//    public int getFromPosition() {
//        return fromPosition;
//    }
//
//    public void setFromPosition(int fromPosition) {
//        this.fromPosition = fromPosition;
//    }

    public String getNameMask() {
        return nameMask;
    }

    public void setNameMask(String nameMask) {
        this.nameMask = nameMask;
    }

    public String getPhoneMask() {
        return phoneMask;
    }

    public void setPhoneMask(String phoneMask) {
        this.phoneMask = phoneMask;
    }

    public String getCarModelMask() {
        return carModelMask;
    }

    public void setCarModelMask(String carModelMask) {
        this.carModelMask = carModelMask;
    }

    public String getCarNumberMask() {
        return carNumberMask;
    }

    public void setCarNumberMask(String carNumberMask) {
        this.carNumberMask = carNumberMask;
    }
}
