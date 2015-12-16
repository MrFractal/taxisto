package ru.trendtech.services.watchers;

import org.joda.money.Money;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.DriverCashFlow;
import ru.trendtech.domain.DriverPeriodWork;
import ru.trendtech.domain.DriverRequisite;
import ru.trendtech.repositories.CashRepository;
import ru.trendtech.repositories.DriverPeriodWorkRepository;
import ru.trendtech.repositories.DriverRequisiteRepository;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.utils.DateTimeUtils;

import java.util.List;

/**
 * Created by petr on 02.02.2015.
 */
@Service("salaryWatcher")
public class SalaryWatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(SalaryWatcher.class);
    @Autowired
    private DriverRequisiteRepository driverRequisiteRepository;
    @Autowired
    private DriverPeriodWorkRepository driverPeriodWorkRepository;
    @Autowired
    private CashRepository driverCashRepository;
    @Autowired
    private AdministrationService administrationService;

//    11 - снятие всех средств с кошелька зарплатного водителя по окончании смены
//    12 - начисление ЗП зарплатному водителю
//    13 - снятие с ЗП за платный отдых


    @Scheduled(fixedRate = 60000)
    @Transactional
    public void salaryWatcher() {
//        /* получаем все активные реквизиты НЕ уволенных водителей */
//        List<DriverRequisite> driverRequisites = driverRequisiteRepository.findByActiveAndTypeDismissal(true, 1);
//          for(DriverRequisite driverRequisite:driverRequisites){
//             Driver driver = driverRequisite.getDriver();
//             DateTime now = DateTimeUtils.nowNovosib_GMT6();
//              List<DriverPeriodWork> driverPeriodWorkList = driverPeriodWorkRepository.findByDriver(driver); // здесь лучше взять не все периоды, а, например, за последние 2 суток
//                 if(!driverPeriodWorkList.isEmpty()){
//                     for(DriverPeriodWork driverPeriodWork :driverPeriodWorkList) {
//                         if(!administrationService.isContainsDate(driverPeriodWork.getStartWork(), driverPeriodWork.getEndWork()) && now.isAfter(driverPeriodWork.getEndWork().getMillis())){
//                             // текущая дата не входит в текущую смену и находится после даты окончания смены - лезем в базу и смотрим есть ли в DriverCashFlow какие-то манипуляции с этой сменой
//                             DriverCashFlow driverCashFlow = driverCashRepository.findByDriverAndDriverPeriodWorkAndOperation(driver, driverPeriodWork, 11);
//                                if(driverCashFlow == null){
//                                   // не было финансовых операций для это смены водителя - создаем
//                                    Money currentDriverMoney = driver.getAccount().getMoney();
//                                    administrationService.operationWithMoney(driver.getId(), null, currentDriverMoney.getAmount().intValue(), 11, null, "", driverPeriodWork.getId());
//                                }
//
//                         }
//                     }
//                 }
//          }
    }
}
