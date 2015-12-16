package ru.trendtech.services.report;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ru.trendtech.common.mobileexchange.model.common.ClientStatsInfo;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.common.mobileexchange.model.common.DriverStatsInfo;
import ru.trendtech.common.mobileexchange.model.web.*;
import ru.trendtech.domain.*;
import ru.trendtech.domain.admin.WebUser;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.*;
import ru.trendtech.services.common.CommonService;
import ru.trendtech.utils.DateTimeUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Created by petr on 09.07.2015.
 */
@Service
@Transactional
public class ReportService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private DriverLockRepository driverLockRepository;
    @Autowired
    private DriverRequisiteRepository driverRequisiteRepository;
    @Autowired
    private ClientLockRepository clientLockRepository;
    @Autowired
    private CommonService commonService;
    @Autowired
    private WebUserRepository webUserRepository;
    private final String COMMENTS = "1";
    private final String REVIEWS = "2";






    public EstimateInfoResponse estimateInfoAll(int numPage, int pageSize, List<QueryDetails> queryDetailsList, String security_token){
        EstimateInfoResponse response = new EstimateInfoResponse();
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
            throw new CustomException(1, "Web user not found");
        }

        Session session = entityManager.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Estimate.class);
        Criteria criteriaCount = session.createCriteria(Estimate.class);

        for (QueryDetails queryDetails : queryDetailsList) {
            switch (queryDetails.getNameParam()) {
                case "comment":{
                    if(queryDetails.getEqual().toString().equals(COMMENTS)){
                        // комменты
                        criteria.add(Restrictions.isNotNull("mission.id"));
                        criteriaCount.add(Restrictions.isNotNull("mission.id"));
                    } else if(queryDetails.getEqual().toString().equals(REVIEWS)){
                        // отзывы
                        criteria.add(Restrictions.isNull("mission"));
                        criteriaCount.add(Restrictions.isNull("mission"));
                    }
                    break;
                }
                case "phoneClient":{
                    criteria.add(Restrictions.eq("client.phone", queryDetails.getEqual()));
                    criteriaCount.add(Restrictions.eq("client.phone", queryDetails.getEqual()));
                    break;
                }
                case "emailClient":{
                    criteria.add(Restrictions.eq("client.email", queryDetails.getEqual()));
                    criteriaCount.add(Restrictions.eq("client.email", queryDetails.getEqual()));
                    break;
                }
                case "nameMask":{
                    criteria.add(Restrictions.or(Restrictions.ilike("client.firstName", "%" + queryDetails.getEqual() + "%"), Restrictions.ilike("client.lastName", "%" + queryDetails.getEqual() + "%"), Restrictions.ilike("driver.firstName", "%" + queryDetails.getEqual() + "%"), Restrictions.ilike("driver.lastName", "%" + queryDetails.getEqual() + "%")));
                    criteriaCount.add(Restrictions.or(Restrictions.ilike("client.firstName", "%" + queryDetails.getEqual() + "%"), Restrictions.ilike("client.lastName", "%" + queryDetails.getEqual() + "%"), Restrictions.ilike("driver.firstName", "%" + queryDetails.getEqual() + "%"), Restrictions.ilike("driver.lastName", "%" + queryDetails.getEqual() + "%")));
                    break;
                }
                case "missionId": {
                    criteria.add(Restrictions.eq("mission.id", Long.valueOf(queryDetails.getEqual().toString())));
                    criteriaCount.add(Restrictions.eq("mission.id", Long.valueOf(queryDetails.getEqual().toString())));
                    break;
                }
                case "driverId": {
                    criteria.add(Restrictions.eq("driver.id", Long.valueOf(queryDetails.getEqual().toString()))); // mission.driverInfo
                    criteriaCount.add(Restrictions.eq("driver.id", Long.valueOf(queryDetails.getEqual().toString())));
                    break;
                }
                case "taxoparkId": {
                    criteria.add(Restrictions.eq("mission.driverInfo.taxoparkPartners.id", Long.valueOf(queryDetails.getEqual().toString())));
                    criteriaCount.add(Restrictions.eq("mission.driverInfo.taxoparkPartners.id", Long.valueOf(queryDetails.getEqual().toString())));
                    break;
                }
                case "clientId": {
                    criteria.add(Restrictions.eq("client.id", Long.valueOf(queryDetails.getEqual().toString()))); // mission.clientInfo.id
                    criteriaCount.add(Restrictions.eq("client.id", Long.valueOf(queryDetails.getEqual().toString())));
                    break;
                }

                case "estimateDate": { // заменить на estimateDate
                    if (queryDetails.getOperationQuery().equals("<")) {
                        criteria.add(Restrictions.lt("estimateDate", DateTimeUtils.toDateTime(Long.valueOf(queryDetails.getEqual().toString())))); // dateTime
                        criteriaCount.add(Restrictions.lt("estimateDate", DateTimeUtils.toDateTime(Long.valueOf(queryDetails.getEqual().toString())))); // dateTime
                    } else if (queryDetails.getOperationQuery().equals(">")) {
                        criteria.add(Restrictions.gt("estimateDate", DateTimeUtils.toDateTime(Long.valueOf(queryDetails.getEqual().toString())))); // dateTime
                        criteriaCount.add(Restrictions.gt("estimateDate", DateTimeUtils.toDateTime(Long.valueOf(queryDetails.getEqual().toString())))); // dateTime
                    } else if (queryDetails.getOperationQuery().equals("=")) {

                    } else if (queryDetails.getOperationQuery().equals("between")) {
                        criteria.add(Restrictions.gt("estimateDate", DateTimeUtils.toDateTime(Long.valueOf(queryDetails.getStart()))));
                        criteria.add(Restrictions.lt("estimateDate", DateTimeUtils.toDateTime(Long.valueOf(queryDetails.getEnd()))));
                        criteriaCount.add(Restrictions.gt("estimateDate", DateTimeUtils.toDateTime(Long.valueOf(queryDetails.getStart()))));
                        criteriaCount.add(Restrictions.lt("estimateDate", DateTimeUtils.toDateTime(Long.valueOf(queryDetails.getEnd()))));
                    }
                    break;
                }
                case "wifiQuality": {
                    if (queryDetails.getOperationQuery().equals("<")) {
                        criteria.add(Restrictions.lt("wifiQuality", Integer.valueOf(queryDetails.getEqual().toString()))); // queryDetails.getEnd()
                        criteriaCount.add(Restrictions.lt("wifiQuality", Integer.valueOf(queryDetails.getEqual().toString()))); //queryDetails.getEnd()
                    } else if (queryDetails.getOperationQuery().equals(">")) {
                        criteria.add(Restrictions.gt("wifiQuality", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.gt("wifiQuality", Integer.valueOf(queryDetails.getEqual().toString())));
                    } else if (queryDetails.getOperationQuery().equals("=")) {
                        criteria.add(Restrictions.eq("wifiQuality", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.eq("wifiQuality", Integer.valueOf(queryDetails.getEqual().toString())));
                    } else if (queryDetails.getOperationQuery().equals("between")) {
                        criteria.add(Restrictions.lt("wifiQuality", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteria.add(Restrictions.gt("wifiQuality", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.lt("wifiQuality", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.gt("wifiQuality", Integer.valueOf(queryDetails.getEqual().toString())));
                    }
                    break;
                }
                case "applicationConvenience": {
                    if (queryDetails.getOperationQuery().equals("<")) {
                        criteria.add(Restrictions.lt("applicationConvenience", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.lt("applicationConvenience", Integer.valueOf(queryDetails.getEqual().toString())));
                    } else if (queryDetails.getOperationQuery().equals(">")) {
                        criteria.add(Restrictions.gt("applicationConvenience", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.gt("applicationConvenience", Integer.valueOf(queryDetails.getEqual().toString())));
                    } else if (queryDetails.getOperationQuery().equals("=")) {
                        criteria.add(Restrictions.eq("applicationConvenience", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.eq("applicationConvenience", Integer.valueOf(queryDetails.getEqual().toString())));
                    } else if (queryDetails.getOperationQuery().equals("between")) {
                        criteria.add(Restrictions.lt("applicationConvenience", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteria.add(Restrictions.gt("applicationConvenience", Integer.valueOf(queryDetails.getEqual().toString())));

                        criteriaCount.add(Restrictions.lt("applicationConvenience", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.gt("applicationConvenience", Integer.valueOf(queryDetails.getEqual().toString())));
                    }
                    break;
                }
                case "driverCourtesy": {
                    if (queryDetails.getOperationQuery().equals("<")) {
                        criteria.add(Restrictions.lt("driverCourtesy", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.lt("driverCourtesy", Integer.valueOf(queryDetails.getEqual().toString())));
                    } else if (queryDetails.getOperationQuery().equals(">")) {
                        criteria.add(Restrictions.gt("driverCourtesy", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.gt("driverCourtesy", Integer.valueOf(queryDetails.getEqual().toString())));
                    } else if (queryDetails.getOperationQuery().equals("=")) {
                        criteria.add(Restrictions.eq("driverCourtesy", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.eq("driverCourtesy", Integer.valueOf(queryDetails.getEqual().toString())));
                    } else if (queryDetails.getOperationQuery().equals("between")) {
                        criteria.add(Restrictions.lt("driverCourtesy", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteria.add(Restrictions.gt("driverCourtesy", Integer.valueOf(queryDetails.getEqual().toString())));

                        criteriaCount.add(Restrictions.lt("driverCourtesy", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.gt("driverCourtesy", Integer.valueOf(queryDetails.getEqual().toString())));
                    }
                    break;
                }
                case "cleanliness": {
                    if (queryDetails.getOperationQuery().equals("<")) {
                        criteria.add(Restrictions.lt("cleanlinessInCar", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.lt("cleanlinessInCar", Integer.valueOf(queryDetails.getEqual().toString())));
                    } else if (queryDetails.getOperationQuery().equals(">")) {
                        criteria.add(Restrictions.gt("cleanlinessInCar", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.gt("cleanlinessInCar", Integer.valueOf(queryDetails.getEqual().toString())));
                    } else if (queryDetails.getOperationQuery().equals("=")) {
                        criteria.add(Restrictions.eq("cleanlinessInCar", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.eq("cleanlinessInCar", Integer.valueOf(queryDetails.getEqual().toString())));
                    } else if (queryDetails.getOperationQuery().equals("between")) {
                        criteria.add(Restrictions.lt("cleanlinessInCar", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteria.add(Restrictions.gt("cleanlinessInCar", Integer.valueOf(queryDetails.getEqual().toString())));

                        criteriaCount.add(Restrictions.lt("cleanlinessInCar", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.gt("cleanlinessInCar", Integer.valueOf(queryDetails.getEqual().toString())));
                    }
                    break;
                }
                case "general": {
                    if (queryDetails.getOperationQuery().equals("<")) {
                        criteria.add(Restrictions.lt("general", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.lt("general", Integer.valueOf(queryDetails.getEqual().toString())));
                    } else if (queryDetails.getOperationQuery().equals(">")) {
                        criteria.add(Restrictions.gt("general", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.gt("general", Integer.valueOf(queryDetails.getEqual().toString())));
                    } else if (queryDetails.getOperationQuery().equals("=")) {
                        criteria.add(Restrictions.eq("general", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.eq("general", Integer.valueOf(queryDetails.getEqual().toString())));
                    } else if (queryDetails.getOperationQuery().equals("between")) {
                        criteria.add(Restrictions.lt("general", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteria.add(Restrictions.gt("general", Integer.valueOf(queryDetails.getEqual().toString())));

                        criteriaCount.add(Restrictions.lt("general", Integer.valueOf(queryDetails.getEqual().toString())));
                        criteriaCount.add(Restrictions.gt("general", Integer.valueOf(queryDetails.getEqual().toString())));
                    }
                    break;
                }
                default:
                    break;
            }
        }
        if(webUser.getTaxoparkId()!=null){
            criteria.add(Restrictions.eq("driver.taxoparkPartners.id", webUser.getTaxoparkId()));
        }
        criteria.add(Restrictions.ne("estimateComment",""));
        criteriaCount.add(Restrictions.ne("estimateComment",""));
        criteria.addOrder(Order.desc("id"));

        Long count = Long.parseLong(criteriaCount.setProjection(Projections.rowCount()).uniqueResult().toString());
        response.setTotalItems(count);

        criteria.setFirstResult((numPage - 1) * pageSize);
        criteria.setMaxResults(pageSize);

        List<Estimate> estimates = criteria.list();

        if(!CollectionUtils.isEmpty(estimates)) {
            for (Estimate estimate : estimates) {
                Mission mission = estimate.getMission();
                response.getEstimateInfoARMList().add(ModelsUtils.toModel(estimate, mission));
            }
        }
        return response;
    }










    @Transactional(propagation = Propagation.REQUIRED)
    public GlobalDriverStatsResponse getDriverGlobalStats(long taxoparkId, long assistantId, long registrationStart, long registrationEnd){

        GlobalDriverStatsResponse response = new GlobalDriverStatsResponse();
        Session session = entityManager.unwrap(Session.class);
        StringBuilder builder = new StringBuilder();
        Map parameters = new HashMap();

            builder.append("from Driver d join d.account a ");
            builder.append(" where 1=1 ");

        if (registrationStart != 0 && registrationEnd != 0) {
            builder.append(" and a.timeCreated >= :startDate and a.timeCreated <= :endDate");
            parameters.put("startDate", new DateTime(new Date(registrationStart * 1000)));
            parameters.put("endDate", new DateTime(new Date(registrationEnd * 1000)));
        } else if (registrationStart != 0 && registrationEnd == 0) {
            builder.append(" and a.timeCreated >= :startDate ");
            parameters.put("startDate", new DateTime(new Date(registrationStart * 1000)));
        } else if (registrationStart == 0 && registrationEnd != 0) {
            builder.append(" and a.timeCreated <= :endDate");
            parameters.put("endDate", new DateTime(new Date(registrationEnd * 1000)));
        }

        if(taxoparkId!=0){
            builder.append(" and d.taxoparkPartners.id = :taxoparkId");
            parameters.put("taxoparkId", taxoparkId);
        }
        if(assistantId!=0){
            builder.append(" and d.assistant.id = :assistantId");
            parameters.put("assistantId", assistantId);
        }


        String hqlQuery = builder.toString();

        LOGGER.info("QUERY: " + hqlQuery);

        Query query = session.createQuery(hqlQuery);
        Iterator iter = parameters.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            Object value = parameters.get(name);
            query.setParameter(name,value);
        }

        List<Object> result = (List<Object>) query.list();

        List<Driver> drivers = new ArrayList<>();
        Iterator itr = result.iterator();
        while(itr.hasNext() ){
            Driver driver = null;
            Object object = itr.next();
            if(object instanceof Driver){
                driver = (Driver) object;
            } else if(object instanceof Object[]){
                Object[] obj = (Object[]) object;
                driver = (Driver)obj[0];
            }
            if(driver!=null){
                drivers.add(driver);
            }
        }


        for(Driver driver: drivers){
            long driverId = driver.getId();
            GlobalDriverStatsInfo globalStat = new GlobalDriverStatsInfo();
            DriverStatsInfo shortStat = getDriverStats(driver, null);
            globalStat.setOwnDriver(!CollectionUtils.isEmpty(driverRequisiteRepository.findByDriverAndActive(driver, true)) ? true : false);
            globalStat.setDriverId(driverId);
            globalStat.setAutoModel(driver.getAutoModel());
            globalStat.setAutoYear(driver.getAutoYear());
            globalStat.setAutoClass(driver.getAutoClass().toString());
            globalStat.setLogin(driver.getLogin());
            globalStat.setFirstName(driver.getFirstName());
            globalStat.setLastName(driver.getLastName());
            globalStat.setPhone(driver.getPhone());
            globalStat.setTaxopark(driver.getTaxoparkPartners() != null ? driver.getTaxoparkPartners().getNameTaxopark() : "");
            globalStat.setAssistant(driver.getAssistant() != null ? driver.getAssistant().getName(): "");
            globalStat.setRegistrationDate(driver.getAccount().getTimeCreated()!=null ? driver.getAccount().getTimeCreated().toLocalDate().toString(): "");
            globalStat.setBalance(driver.getAccount().getMoney().getAmount().intValue());
            globalStat.setRating((int) driver.getRating());
            globalStat.setCountMission(shortStat.getCountMission());
            globalStat.setAllSummMissionMoney(shortStat.getAllSummMissionMoney());

            List<DriverLocks> driverLocks = driverLockRepository.findByDriverIdAndTimeOfUnlockIsNull(driverId, new PageRequest(0, 1));
            DriverLocks locks = null;
            if(!CollectionUtils.isEmpty(driverLocks)){
                locks = driverLocks.get(0);
            }
            globalStat.setBlockState(locks!=null ? "заблокирован":"не заблокирован");
            globalStat.setDateOfBlock(locks!=null ? DateTimeUtils.toDateTime(locks.getTimeOfLock()).toString():"");
            globalStat.setReasonOfBlock(locks!=null ? locks.getReason():"");
            globalStat.setRatingAVG((int) driver.getRating());
            globalStat.setAverageCheckMoney(shortStat.getAverageCheckMoney());
            globalStat.setAverageCountMissionMonth(shortStat.getAverageCountMissionMonth());

            if(shortStat.getCountMission()!=0){
                globalStat.setPercentRatingOnCountOrder(driver.getRatedMissions()/shortStat.getCountMission());
                globalStat.setPercentCommentOnCountOrder(countAVGEstimateByDriver(driver)/shortStat.getCountMission());
                globalStat.setAverageTimeArrivingInFact(averageTimeArrivingInFact(driverId) / shortStat.getCountMission());
                globalStat.setAverageTimeArriving(averageTimeArriving(driverId)/shortStat.getCountMission());
                globalStat.setPercentPushLateDriver(countPushLateDriver(driverId)/shortStat.getCountMission());
                globalStat.setCountRepeatOrderWithClient(countRepeat(driver)/shortStat.getCountMission());
            }
                globalStat.setDateOfFirstOrder(firstOrderByDriver(driver));
                globalStat.setDateOfLastOrder(lastOrderByDriver(driver));
                globalStat.setCountCanceledMissionByClient(countCancelByDriver(driverId, session));
                globalStat.setCountCanceledMissionByDriver(countCancelByDriver(driverId, session));
                globalStat.setCountOrderWithPromo(countMissionByPromo(driver));

            response.getGlobalDriverStatsInfos().add(globalStat);
        }
        return response;
    }





    public DriverStatsInfo getDriverStats(Driver driver, Long taxoparkId){
        DriverStatsInfo driverStatsInfo = new DriverStatsInfo();
        Session session = entityManager.unwrap(Session.class);
        boolean taxopark = taxoparkId!=null ? true: false;

        //   общее количество заказов   //
        Query query = session.createSQLQuery("select count(*) from mission m join driver drv on m.driverInfo_id=drv.id where m.driverInfo_id = :id and m.state=:state "+(taxopark?"and drv.taxopark_id=:taxoparkId":""));
        query.setParameter("id", driver.getId());
        query.setParameter("state", "COMPLETED");
        if(taxopark){
            query.setParameter("taxoparkId", taxoparkId);
        }
        BigInteger resultAllCount = (BigInteger)query.uniqueResult();
        driverStatsInfo.setCountMission(resultAllCount.intValue());


        //  среднее кол-во заказов за месяц  //
        query = session.createSQLQuery("select count(*)/count(distinct date_format(m.time_finishing,'%Y-%m')) from mission m join driver drv on m.driverInfo_id=drv.id where driverInfo_id = :id and m.state=:state and m.test_order=0 "+(taxopark?"and drv.taxopark_id=:taxoparkId":""));
        query.setParameter("id", driver.getId());
        query.setParameter("state", "COMPLETED");
        if(taxopark){
            query.setParameter("taxoparkId", taxoparkId);
        }
        BigDecimal un = (BigDecimal)query.uniqueResult();
        if(un!=null)
            driverStatsInfo.setAverageCountMissionMonth(un.doubleValue());


        //   Кол-во за текущий месяц   //
        query = session.createSQLQuery("SELECT count(*) from mission m join driver drv on m.driverInfo_id=drv.id where m.driverInfo_id = :id and m.state=:state and m.time_finishing > LAST_DAY(CURDATE()) + INTERVAL 1 DAY - INTERVAL 1 MONTH" +
                " AND  m.time_finishing < DATE_ADD(LAST_DAY(CURDATE()), INTERVAL 1 DAY) and m.test_order=0 "+(taxopark?"and drv.taxopark_id=:taxoparkId":""));
        query.setParameter("id", driver.getId());
        query.setParameter("state", "COMPLETED");
        if(taxopark){
            query.setParameter("taxoparkId", taxoparkId);
        }
        BigInteger countNowMonths = (BigInteger)query.uniqueResult();
        driverStatsInfo.setCountMissionCurrentMonth(countNowMonths.intValue());


            /* сумма в месяц (в деньгах) (текущий)*/
        query = session.createSQLQuery("SELECT sum(m.price_in_fact_amount) from mission m join driver drv on m.driverInfo_id=drv.id where driverInfo_id = :id and m.state=:state and m.time_finishing > LAST_DAY(CURDATE()) + INTERVAL 1 DAY - INTERVAL 1 MONTH" +
                " AND m.time_finishing < DATE_ADD(LAST_DAY(CURDATE()), INTERVAL 1 DAY) and m.test_order=0 "+(taxopark?"and drv.taxopark_id=:taxoparkId":""));
        query.setParameter("id", driver.getId());
        query.setParameter("state","COMPLETED");
        if(taxopark){
            query.setParameter("taxoparkId", taxoparkId);
        }
        BigDecimal sumMonths = (BigDecimal)query.uniqueResult();
        if(sumMonths!=null)
            driverStatsInfo.setSummMissionMoneyCurrentMonth(sumMonths.doubleValue());


            /* общая сумма заказов (в деньгах) */
        query = session.createSQLQuery("SELECT SUM(m.price_in_fact_amount) from mission m join driver drv on m.driverInfo_id=drv.id where m.driverInfo_id = :id and m.state=:state and m.test_order=0 "+(taxopark?"and drv.taxopark_id=:taxoparkId":""));
        query.setParameter("id", driver.getId());
        query.setParameter("state", "COMPLETED");
        if(taxopark){
            query.setParameter("taxoparkId", taxoparkId);
        }
        BigDecimal allSum = (BigDecimal)query.uniqueResult();
        if(allSum!=null){
            driverStatsInfo.setAllSummMissionMoney(allSum.doubleValue());
        }else{
            driverStatsInfo.setAllSummMissionMoney(0);
        }


            /* средний чек вообще (в деньгах),  - сумма заказов (точно так же как и среднее кол-во в месяц) */
        query = session.createSQLQuery("select sum(m.price_in_fact_amount)/count(*) from mission m join driver drv on m.driverInfo_id=drv.id where m.driverInfo_id = :id and m.state=:state and m.test_order=0 "+(taxopark?"and drv.taxopark_id=:taxoparkId":""));
        query.setParameter("id", driver.getId());
        query.setParameter("state", "COMPLETED");
        if(taxopark){
            query.setParameter("taxoparkId", taxoparkId);
        }
        BigDecimal bd = (BigDecimal)query.uniqueResult();
        if(bd!=null){
            driverStatsInfo.setAverageCheckMoney(bd.doubleValue());
        }else{
            driverStatsInfo.setAverageCheckMoney(0);
        }

            /* средняя сумма в месяц (в деньгах) за весь период !!!!!!! */
        query = session.createSQLQuery("SELECT sum(m.price_in_fact_amount)/count(distinct date_format(m.time_finishing,'%Y-%m')) from mission m join driver drv on m.driverInfo_id=drv.id where m.driverInfo_id = :id and m.state=:state and m.test_order=0 "+(taxopark?"and drv.taxopark_id=:taxoparkId":""));
        query.setParameter("id", driver.getId());
        query.setParameter("state", "COMPLETED");
        if(taxopark){
            query.setParameter("taxoparkId", taxoparkId);
        }
        BigDecimal avgAllSum = (BigDecimal)query.uniqueResult();
        if(avgAllSum!=null){
            driverStatsInfo.setAllAverageSummMissionMoneyMonth(avgAllSum.doubleValue());
        }else{
            driverStatsInfo.setAllAverageSummMissionMoneyMonth(0);
        }

        query = session.createSQLQuery("SELECT avg(m.price_in_fact_amount) from mission m join driver drv on m.driverInfo_id=drv.id where m.driverInfo_id = :id and m.state = :state and m.time_finishing > LAST_DAY(CURDATE()) + INTERVAL 1 DAY - INTERVAL 1 MONTH" +
                " AND  m.time_finishing < DATE_ADD(LAST_DAY(CURDATE()), INTERVAL 1 DAY) and m.test_order=0 "+(taxopark?"and drv.taxopark_id=:taxoparkId":""));
        query.setParameter("id", driver.getId());
        query.setParameter("state", "COMPLETED");
        if(taxopark){
            query.setParameter("taxoparkId", taxoparkId);
        }
        BigDecimal avgSumLastMonth = (BigDecimal)query.uniqueResult();
        if(avgSumLastMonth!=null){
            driverStatsInfo.setAverageSummMissionMoneyCurrentMonth(avgSumLastMonth.doubleValue());
        }else{
            driverStatsInfo.setAverageSummMissionMoneyCurrentMonth(0);
        }
            /* блокировки */
        driverStatsInfo.setCountBlock((int)(driverLockRepository.findCountByDriverId(driver.getId()))) ;

        return driverStatsInfo;
    }




    private String findMotive(Client client){
        String motive = "";
        if(client.getRegistrationTime()!=null){
            DateTime plus = client.getRegistrationTime().plusHours(24);
            Session session = entityManager.unwrap(Session.class);
            Query q = session.createSQLQuery("SELECT count(*) FROM client_activated_promo_codes where client_id=:id and date_of_used between :s and :e");
            q.setParameter("id", client.getId());
            q.setParameter("s", client.getRegistrationTime().getMillis());
            q.setParameter("e", plus.getMillis());

            BigInteger count = (BigInteger)q.uniqueResult();
            if(count!=null && count.intValue()>0){
                motive = "промокод";
            }
        }
        return motive;
    }

    public int countLikeFromMission(long clientId, Session session){
        Query q = session.createSQLQuery("SELECT Sum(c) FROM (SELECT id, COUNT(*) AS c\n" +
                "        from mission where from_address is not null and state='COMPLETED' and clientInfo_id=:id group by from_address\n" +
                "        having count(*)>1) as b");
        q.setParameter("id", clientId);
        BigDecimal count = (BigDecimal)q.uniqueResult();
        return count!=null ? count.intValue():0;
    }


    public int countLikeToMission(long clientId, Session session){
        Query q = session.createSQLQuery("SELECT Sum(c) FROM (SELECT COUNT(*) AS c from mission where to_address is not null and state='COMPLETED' and clientInfo_id=:id group by to_address having count(*)>1) as b");
        q.setParameter("id", clientId);
        BigDecimal count = (BigDecimal)q.uniqueResult();
        return count!=null ? count.intValue():0;

    }



    private int countEqualMission(long clientId, Session session){
        Query q = session.createSQLQuery("select sum(count) from(select m2.from_address as fromAddress, m2.to_address as toAddress,\n" +
                "(select count(*) from mission m1 where m1.from_address = m2.from_address and m1.to_address = m2.to_address\n" +
                "and m1.state='COMPLETED' and m1.clientInfo_id=:clientId) as count from mission m2 where m2.state='COMPLETED' and m2.clientInfo_id=:clientId group by fromAddress having count>1) as result");
        q.setParameter("clientId", clientId);
        BigDecimal count = (BigDecimal)q.uniqueResult();
        return count!=null ? count.intValue():0;
    }



    private int countServices(long clientId, Session session){
        Query q = session.createSQLQuery("select s.mission_id from services_expected s\n" +
                "        inner join mission m on m.id=s.mission_id\n" +
                "        where m.state = 'COMPLETED' and m.clientInfo_id = :id group by m.id");
        q.setParameter("id", clientId);
        List result = q.list();
        return result!=null ? result.size():0;
    }



    private int countWatchMission(long clientId, Session session){
        Query q = session.createSQLQuery("SELECT count(*) FROM mission_state_statistic s \n" +
                "inner join mission m on m.id = s.mission_id and m.state='COMPLETED'\n" +
                "where s.state='WATCH_MISSION' and m.clientInfo_id = :id");
        q.setParameter("id", clientId);
        BigInteger count = (BigInteger)q.uniqueResult();
        return count!=null ? count.intValue():0;
    }


    private int countAutosearch(long clientId, Session session){
        Query q = session.createSQLQuery("SELECT count(*) FROM mission_state_statistic s \n" +
                "inner join mission m on m.id=s.mission_id and m.state='COMPLETED'\n" +
                "where s.state='AUTO_SEARCH' and m.clientInfo_id= :id");
        q.setParameter("id", clientId);
        BigInteger count = (BigInteger)q.uniqueResult();
        return count!=null ? count.intValue():0;
    }


    private int countPromoActivated(long clientId, Session session){
        Query q = session.createSQLQuery("SELECT count(*) FROM promo_codes where to_id = :id");
        q.setParameter("id", clientId);
        BigInteger count = (BigInteger)q.uniqueResult();
        return count!=null ? count.intValue():0;
    }


    private int countPromoSend(long clientId, Session session){
        Query q = session.createSQLQuery("SELECT count(*) FROM promo_codes where from_id = :id");
        q.setParameter("id", clientId);
        BigInteger count = (BigInteger)q.uniqueResult();
        return count!=null ? count.intValue():0;
    }


    private int avgRate(long clientId, Session session){
        Query q = session.createSQLQuery("SELECT avg(general) FROM estimate where general != 0 and client_id = :id");
        q.setParameter("id", clientId);
        BigDecimal count = (BigDecimal)q.uniqueResult();
        return count!=null ? count.intValue():0;
    }


    private int countClientEstimate(long clientId, Session session){
        Query q = session.createSQLQuery("SELECT count(*) FROM estimate where general!=0 and client_id = :id");
        q.setParameter("id", clientId);
        BigInteger count = (BigInteger)q.uniqueResult();
        return count.intValue();
    }


    private int countClientComments(long clientId, Session session){
        Query q = session.createSQLQuery("SELECT count(*) FROM estimate where estimate_comment!='' and client_id = :id");
        q.setParameter("id", clientId);
        BigInteger count = (BigInteger)q.uniqueResult();
        return count.intValue();
    }


    private int countMissionByAutoClass(long clientId, String autoClass, Session session){
        Query q = session.createSQLQuery(" SELECT count(*) FROM mission where clientInfo_id = :id and auto_class = :autoClass and state='COMPLETED'");
        q.setParameter("id", clientId);
        q.setParameter("autoClass", autoClass);
        BigInteger count = (BigInteger)q.uniqueResult();
        return count.intValue();
    }


    private int countRepeat(Driver driver){
        Session session = entityManager.unwrap(Session.class);
        Query q = session.createSQLQuery(" SELECT Sum(c)\n" +
                "   FROM (SELECT id, COUNT(clientInfo_id) AS c\n" +
                "      FROM mission where driverInfo_id= :id\n" +
                "      GROUP BY clientInfo_id having c>1) AS b;");
        q.setParameter("id", driver.getId());
        BigDecimal count = (BigDecimal)q.uniqueResult();
        return count!=null ? count.intValue():0;
    }


    private int countPushLateDriver(long driverId){
        Session session = entityManager.unwrap(Session.class);
        Query q = session.createSQLQuery("select count(*) as c from expected_arrival_times e inner join mission m on m.id=e.mission_id and m.driverInfo_id= :id and m.state='COMPLETED'");
        q.setParameter("id", driverId);
        BigInteger count = (BigInteger)q.uniqueResult();
        return count!=null ? count.intValue():0;
    }


    private int averageTimeArriving(long driverId){
        Session session = entityManager.unwrap(Session.class);
        Query q = session.createSQLQuery("SELECT Sum(c)/60 FROM (select abs(unix_timestamp(time_starting)-unix_timestamp(IFNULL(DATE_ADD(m.time_assigning, INTERVAL sum(ex.expectedArrivalTimes) MINUTE), m.time_assigning))) as c from mission m\n" +
                "left join expected_arrival_times ex on m.id = ex.mission_id\n" +
                "where m.driverInfo_id = :id and m.state='COMPLETED' group by ex.mission_id) as b");
        q.setParameter("id", driverId);
        BigDecimal count = (BigDecimal)q.uniqueResult();

        return count!=null ? count.intValue():0;
    }


    private int averageTimeArrivingInFact(long driverId){
        Session session = entityManager.unwrap(Session.class);
        Query q = session.createSQLQuery("SELECT Sum(c)/60 FROM (select abs(unix_timestamp(time_starting)-unix_timestamp(time_arriving)) AS c from mission where driverInfo_id= :id and state = 'COMPLETED') as b");
        q.setParameter("id", driverId);
        BigDecimal count = (BigDecimal)q.uniqueResult();
        return count!= null ? count.intValue():0;
    }


    private int countMissionByPromo(Driver driver){
        Session session = entityManager.unwrap(Session.class);
        Query q = session.createSQLQuery("SELECT count(*) FROM driver_cash_flow cash where cash.driver_id= :id and cash.operation=5");
        q.setParameter("id", driver.getId());
        BigInteger count = (BigInteger)q.uniqueResult();
        return count.intValue();
    }





    private int countCancelByClientWithoutDriver(long clientId,Session session){
        Query q = session.createSQLQuery("SELECT count(*) FROM mission_canceled c\n" +
                "    inner join mission m on m.id=c.mission_id\n" +
                "    and m.clientInfo_id = :id and m.state='CANCELED' and c.cancel_by = 'client' and m.driverInfo_id is null");
        q.setParameter("id", clientId);
        BigInteger count = (BigInteger)q.uniqueResult();
        return count.intValue();
    }


    private int countCancelByClientWithDriver(long clientId, Session session){
        Query q = session.createSQLQuery("SELECT count(*) FROM mission_canceled c\n" +
                "    inner join mission m on m.id=c.mission_id\n" +
                "    and m.clientInfo_id = :id and m.state='CANCELED' and c.cancel_by = 'client' and m.driverInfo_id is not null");
        q.setParameter("id", clientId);
        BigInteger count = (BigInteger)q.uniqueResult();
        return count.intValue();
    }


    private int countCancelByDriver(long driverId, Session session){
        Query q = session.createSQLQuery("SELECT count(*) FROM mission_canceled c\n" +
                "    inner join mission m on m.id=c.mission_id\n" +
                "    and m.driverInfo_id = :id and m.state='CANCELED' and c.cancel_by= 'driver'");
        q.setParameter("id", driverId);
        BigInteger count = (BigInteger)q.uniqueResult();
        return count.intValue();
    }


    private int countCancelByclient(long clientId, Session session){
        Query q = session.createSQLQuery("SELECT count(*) FROM mission_canceled c\n" +
                "    inner join mission m on m.id=c.mission_id\n" +
                "    and m.driverInfo_id = :id and m.state='CANCELED' and c.cancel_by= 'client'");
        q.setParameter("id", clientId);
        BigInteger count = (BigInteger)q.uniqueResult();
        return count.intValue();
    }


    private String lastOrderByClient(Client client){
        Session session = entityManager.unwrap(Session.class);
        Query q = session.createSQLQuery("select MAX(m.time_finishing) from mission m where m.state = 'COMPLETED' and m.clientInfo_id= :id and test_order=0");
        q.setParameter("id", client.getId());
        return q.uniqueResult()!=null? q.uniqueResult().toString():"";
    }


    private String firstOrderByClient(Client client){
        Session session = entityManager.unwrap(Session.class);
        Query q = session.createSQLQuery("select MIN(m.time_finishing) from mission m where m.state = 'COMPLETED' and m.clientInfo_id= :id and test_order=0");
        q.setParameter("id", client.getId());
        return q.uniqueResult()!=null? q.uniqueResult().toString():"";
    }


    private String lastOrderByDriver(Driver driver){
        Session session = entityManager.unwrap(Session.class);
        Query q = session.createSQLQuery("select MAX(m.time_finishing) from mission m where m.state = 'COMPLETED' and m.driverInfo_id= :id and test_order=0");
        q.setParameter("id", driver.getId());
        return q.uniqueResult()!=null? q.uniqueResult().toString():"";
    }


    private String firstOrderByDriver(Driver driver){
        Session session = entityManager.unwrap(Session.class);
        Query q = session.createSQLQuery("select MIN(m.time_finishing) from mission m where m.state = 'COMPLETED' and m.driverInfo_id= :id and test_order=0");
        q.setParameter("id", driver.getId());
        return q.uniqueResult()!=null? q.uniqueResult().toString():"";
    }


    private int countAVGEstimateByDriver(Driver driver){
        Session session = entityManager.unwrap(Session.class);
        Query q1 = session.createSQLQuery("select count(case when m.state = 'COMPLETED' and m.driverInfo_id= :id and m.estimate_comment is not NULL and test_order=0 then 1 end) as completed_cnt from mission m");
        q1.setParameter("id", driver.getId());
        BigInteger count = (BigInteger)q1.uniqueResult();
        return  count.intValue();
    }




    public BukanovReportResponse bukanovReport(String security_token, long startTime, long endTime){
        WebUser webUser = webUserRepository.findByToken(security_token);
        if(webUser == null){
             throw new CustomException(1, "Web user not found");
        }
        BukanovReportResponse response = new BukanovReportResponse();
        String timeParam = commonService.getPropertyValue("time_parameter");

        StringBuilder query = new StringBuilder();
        query.append(
                        /* 0 - allCount */
                        "select count(*) as allCount from mission where (unix_timestamp(time_requesting)+21600) between "+startTime+" and "+endTime+"\n" +

                        /* 1 - outerCount */
                        "\n union all \n" +
                        "select count(*) as outerCount from mission where test_order=0 and (unix_timestamp(time_requesting)+21600) between "+startTime+" and "+endTime+" \n" +

                        /* 2 - innerCount */
                        "\n union all \n" +
                        "select count(*) as innerCount from mission where test_order=1 and (unix_timestamp(time_requesting)+21600) between "+startTime+" and "+endTime+"\n" +

                        /* 3 - doubleCount */
                        "\n union all \n" +
                        /*
                        "select sum(count) from (select count(*) as count, concat(m.from_address, m.to_address, m.clientInfo_id) as res, m.clientInfo_id, m.from_address, m.to_address, m.id \n" +
                        "from mission m where test_order=0 and (unix_timestamp(time_requesting)+21600) between "+startTime+" and "+endTime+" group by res\n" +
                        "having count>1) as doubleCount "+
                        */
                        "select count(*) from(select count(*) as count, m.clientInfo_id, m.from_address, m.to_address, m.id \n" +
                                "from mission m where m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" group by m.from_address, m.to_address, m.clientInfo_id\n" +
                                "having count>1) as doubleCount"+


                        /* 4 - countWithDriver */
                        "\n union all \n" +
                        //"SELECT count(*) as countWithDriver FROM mission m where m.driverInfo_id is not null and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+"\n" +
                        "select count(*) from (select count(*) as count, m.clientInfo_id, m.from_address, m.to_address, m.driverInfo_id \n" +
                        "from mission m where test_order=0 and m.driverInfo_id is not null and (unix_timestamp(time_requesting)+21600) between "+startTime+" and "+endTime+" group by m.from_address, m.to_address, m.clientInfo_id\n" +
                        ") as countWithDriver"+



                        /* 5 - countWithDriverAndCompleted */
                        "\n union all \n" +
                        "select count(*) from (select count(*) as count, m.clientInfo_id, m.from_address, m.to_address, m.driverInfo_id, m.state \n" +
                        " from mission m where test_order=0 and m.driverInfo_id is not null and m.state='COMPLETED' and  (unix_timestamp(m."+timeParam+")+21600) between "+startTime+" and "+endTime+" group by m.from_address, m.to_address, m.clientInfo_id\n" +
                        ") as countWithDriverAndCompleted "+
                        /* where 1=1 and countWithDriverAndCompleted.driverInfo_id is not null and countWithDriverAndCompleted.state='COMPLETED' */

                        /* 6 - countWithDriverAndCanceled */
                        "\n union all \n" +
                        /*
                        "select sum(count) from (select count(*) as count, concat(m.from_address, m.to_address, m.clientInfo_id) as res, m.clientInfo_id, m.from_address, m.to_address, m.driverInfo_id, m.state \n" +
                        "from mission m where test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" group by res\n" +
                        "having count=1) as countWithDriverAndCanceled where 1=1 and countWithDriverAndCanceled.driverInfo_id is not null and countWithDriverAndCanceled.state='CANCELED'"+
                        */
                        "select count(*) from (select count(*) as count, m.clientInfo_id, m.from_address, m.to_address, m.driverInfo_id, m.state, m.id as missionId \n" +
                        " from mission m " +
                        " join mission_canceled canc on canc.mission_id=m.id "+
                        " where m.test_order=0 and m.driverInfo_id is not null and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" group by m.from_address, m.to_address, m.clientInfo_id, canc.cancel_by\n" +
                        ") as countWithDriverAndCanceled\n" +



                        /* 7 - countWithDriverAndCanceledByClient */
                        "\n union all \n" +
                        "select count(*) from (select count(*) as count, m.clientInfo_id, m.from_address, m.to_address, m.driverInfo_id, m.state, m.id as missionId \n" +
                        " from mission m " +
                        " join mission_canceled canc on canc.mission_id=m.id and canc.cancel_by='client' "+
                        " where m.test_order=0 and m.driverInfo_id is not null and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" group by m.from_address, m.to_address, m.clientInfo_id, canc.cancel_by\n" +
                        ") as countWithDriverAndCanceledByClient\n" +



                        /* 8 - countWithDriverAndCanceledByDriver */
                        "\n union all \n" +
                        "select count(*) from (select count(*) as count, m.clientInfo_id, m.from_address, m.to_address, m.driverInfo_id, m.state, m.id as missionId \n" +
                        " from mission m " +
                        " join mission_canceled canc on canc.mission_id=m.id and canc.cancel_by='driver' "+
                        " where m.test_order=0 and m.driverInfo_id is not null and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" group by m.from_address, m.to_address, m.clientInfo_id, canc.cancel_by\n" +
                        ") as countWithDriverAndCanceledByDriver\n" +



                        /* 9 - countWithDriverAndCanceledByOperator */
                        "\n union all \n" +
                        "select count(*) from (select count(*) as count, m.clientInfo_id, m.from_address, m.to_address, m.driverInfo_id, m.state, m.id as missionId \n" +
                        " from mission m " +
                        " join mission_canceled canc on canc.mission_id=m.id and canc.cancel_by='operator' "+
                        " where m.test_order=0 and m.driverInfo_id is not null and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" group by m.from_address, m.to_address, m.clientInfo_id, canc.cancel_by\n" +
                        ") as countWithDriverAndCanceledByOperator\n" +
                        //"join mission_canceled canc on canc.mission_id=countWithDriverAndCanceledByOperator.missionId and canc.cancel_by='operator'\n" +



                        /* 10 - countWithoutDriver */
                        "\n union all \n" +
                        "select count(*) from (select count(*) as count, m.driverInfo_id, m.time_requesting, m.id as missionId \n" +
                        "from mission m " +
                        " join mission_canceled canc on canc.mission_id=m.id "+
                        " where m.test_order=0 and m.driverInfo_id is null and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" group by m.from_address, m.to_address, m.clientInfo_id, canc.cancel_by\n" +
                        ") as countWithoutDriver\n" +


                        /* 11 - countWithoutDriverAndCanceledWhenMissionAfterHalfMinutes */
                        "\n union all \n" +
                        "select count(*) from (select count(*) as count, m.clientInfo_id, m.from_address, m.to_address, m.driverInfo_id, m.time_requesting, m.id as missionId, m.region_id as regionId \n" +
                        " from mission m" +
                        " join mission_canceled canc on canc.mission_id=m.id "+
                        " where m.test_order=0 and m.driverInfo_id is null and (unix_timestamp(canc.time_of_canceled))-(unix_timestamp(m.time_requesting))>30 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" group by m.from_address, m.to_address, m.clientInfo_id, canc.cancel_by\n" +
                        " ) as countWithoutDriverAndCanceledWhenMissionAfterHalfMinutes\n" +
                        //" join mission_canceled canc on canc.mission_id=countWithoutDriverAndCanceledWhenMissionAfterHalfMinutes.missionId and canc.state_before_canceled!='AUTO_SEARCH'\n" +
                        //" where (unix_timestamp(canc.time_of_canceled))-(unix_timestamp(countWithoutDriverAndCanceledWhenMissionAfterHalfMinutes.time_requesting))>=30 "+



                        /* 12 - countWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch */
                        "\n union all \n" +
                        "select count(*) from (select count(*) as count, m.clientInfo_id, m.from_address, m.to_address, m.driverInfo_id, m.time_requesting, m.id as missionId, m.region_id as regionId  \n" +
                        "        from mission m " +
                        "        join mission_canceled canc on canc.mission_id=m.id and canc.state_before_canceled='AUTO_SEARCH' and canc.cancel_by='client' \n" +
                        " where m.test_order=0 and m.driverInfo_id is null and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" group by m.from_address, m.to_address, m.clientInfo_id, canc.cancel_by\n" +
                        "        ) as countWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch\n" +




                        /* 13 - countWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes */
                        "\n union all \n" +
                        "select count(*) from (select count(*) as count, m.clientInfo_id, m.from_address, m.to_address, m.driverInfo_id, m.time_requesting, m.id as missionId, m.region_id as regionId \n" +
                        "        from mission m " +
                        "        join mission_canceled canc on canc.mission_id=m.id and canc.cancel_by='client' \n"+
                        " where m.test_order=0 and m.driverInfo_id is null and (unix_timestamp(canc.time_of_canceled))-(unix_timestamp(m.time_requesting))<=30 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" group by m.from_address, m.to_address, m.clientInfo_id, canc.cancel_by\n" +
                        "        ) as countWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes\n" +
                        //"        join region r on r.id=countWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes.regionId and r.type_region!=2 \n"+
                        //"        where (unix_timestamp(canc.time_of_canceled))-(unix_timestamp(countWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes.time_requesting))<30"+


                        /* 14 - countFromRemoteZone  [dev reborne]!!!*/
                        "\n union all \n" +
                        /*
                        "select sum(count) from (select count(*) as count, concat(m.from_address, m.to_address, m.clientInfo_id) as res, m.driverInfo_id, m.region_id as regionId \n" +
                        "        from mission m where test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" group by res having count=1) as countFromRemoteZone\n" +
                        "        join region r on r.id=countFromRemoteZone.regionId\n" +
                        "        where 1=1 and countFromRemoteZone.driverInfo_id is null and r.type_region=2"+
                        */
                        "select count(*) from (select count(*) as count, m.clientInfo_id, m.from_address, m.to_address, m.driverInfo_id, m.time_requesting, m.id as missionId, m.region_id as regionId \n" +
                                "      from mission m" +
                                "      join region r on r.id=m.region_id and r.type_region=2\n"+
                                "      join mission_canceled canc on canc.mission_id=m.id "+
                                " where m.test_order=0 and m.driverInfo_id is null and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" group by m.from_address, m.to_address, m.clientInfo_id, canc.cancel_by\n" +
                                "      ) as countFromRemoteZone \n" +



                        /* 15 - uniqueCount */
                        "\n union all \n" +
                        " select count(*) from (select count(*) as count, m.clientInfo_id, m.from_address, m.to_address, m.id \n" +
                        " from mission m where m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" group by m.from_address, m.to_address, m.clientInfo_id\n" +
                        ") as uniqueCount "+


                        /* 16 - countWithDriverAndCanceledByServer */
                        "\n union all \n" +
                        "select count(*) from (select count(*) as count, m.clientInfo_id, m.from_address, m.to_address, m.driverInfo_id, m.state, m.id as missionId \n" +
                        "from mission m " +
                        " join mission_canceled canc on canc.mission_id=m.id and canc.cancel_by='server'\n"+
                        " where m.test_order=0 and m.driverInfo_id is not null and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" group by m.from_address, m.to_address, m.clientInfo_id, canc.cancel_by\n" +
                        ") as countWithDriverAndCanceledByServer\n"

        );



        /*

        old

        query.append("select *, (outerCount-doubleCount) as uniqueCount, round((ifnull(countWithoutDriver, 0)/(ifnull(countWithoutDriver, 0)+ifnull(countWithDriver, 0)))*100, 2) as mainPercent from(\n" +
                "\n" +
                "select count(*) as allCount, \n" +
                "\n" +
                "(select count(*) from mission m1 where m1.test_order=0 and (unix_timestamp(m1.time_requesting)+21600) between "+startTime+" and "+endTime+") as outerCount, \n" +
                " \n" +
                "(select count(*) from mission m where m.test_order=1 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+") as innerCount,\n" +
                "\n" +
                "(select sum(count) from(select * from(\n" +
                "             select m2.from_address as fromAddress, m2.to_address as toAddress,\n" +
                "             (select count(*)-1 from mission m1 where m1.from_address = m2.from_address and m1.to_address = m2.to_address and m1.clientInfo_id = m2.clientInfo_id and (unix_timestamp(m1.time_requesting)+21600) between "+startTime+" and "+endTime+") as count\n" +
                "             from mission m2 where (unix_timestamp(m2.time_requesting)+21600) between "+startTime+" and "+endTime+") as result group by concat(result.fromAddress, result.toAddress) having count>0) as doubleC) as doubleCount,\n" +
                "             \n" +
                "(SELECT count(*) FROM mission m where m.driverInfo_id is not null and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+") as countWithDriver,\n" +
                "\n" +
                "(SELECT count(*) FROM mission m where m.driverInfo_id is not null and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" and m.state='COMPLETED') as countWithDriverAndCompleted,\n" +
                "\n" +
                "(SELECT count(*) FROM mission_canceled c\n" +
                "                   inner join mission m on m.id=c.mission_id and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+"\n" +
                "                   and m.driverInfo_id is not null and (unix_timestamp(c.time_of_canceled)+21600) between "+startTime+" and "+endTime+") as countWithDriverAndCanceled,\n" +
                "                   \n" +
                "(SELECT count(*) FROM mission_canceled c\n" +
                "                   inner join mission m on m.id=c.mission_id and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+"\n" +
                "                   and c.cancel_by='client' and m.driverInfo_id is not null and (unix_timestamp(c.time_of_canceled)+21600) between "+startTime+" and "+endTime+") as countWithDriverAndCanceledByClient,\n" +
                "                   \n" +
                "(SELECT count(*) FROM mission_canceled c\n" +
                "                   inner join mission m on m.id=c.mission_id and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+"\n" +
                "                   and c.cancel_by='driver' and m.driverInfo_id is not null and (unix_timestamp(c.time_of_canceled)+21600) between "+startTime+" and "+endTime+") as countWithDriverAndCanceledByDriver,\n" +
                "                   \n" +
                "(SELECT count(*) FROM mission_canceled c\n" +
                "                   inner join mission m on m.id=c.mission_id and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+"\n" +
                "                   and c.cancel_by='operator' and m.driverInfo_id is not null and (unix_timestamp(c.time_of_canceled)+21600) between "+startTime+" and "+endTime+") as countWithDriverAndCanceledByOperator,\n" +
                "                   \n" +
                "(SELECT count(*) FROM mission_canceled c\n" +
                "     join mission m on m.id=c.mission_id and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+"\n" +
                "     where m.driverInfo_id is null and (unix_timestamp(c.time_of_canceled)+21600) between "+startTime+" and "+endTime+") as countWithoutDriver,\n" +
                "\n" +
                "\n" +
                "(SELECT count(*) FROM mission_canceled c\n" +
                "     join mission m on m.id=c.mission_id and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+"\n" +
                "     where m.driverInfo_id is null and (unix_timestamp(c.time_of_canceled))-(unix_timestamp(m.time_requesting)) between 30 and 60 and  (unix_timestamp(c.time_of_canceled)+21600) between "+startTime+" and "+endTime+") as countWithoutDriverAndCanceledWhenMissionAfterHalfMinutes,\n" +
                "\n" +
                "(SELECT count(*) FROM mission_canceled c\n" +
                "     join mission m on m.id=c.mission_id and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+"\n" +
                "     where m.driverInfo_id is null and c.cancel_by='client' and c.state_before_canceled='AUTO_SEARCH' and  (unix_timestamp(c.time_of_canceled)+21600) between "+startTime+" and "+endTime+") as countWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch,\n" +
                "                   \n" +
                "(SELECT count(*) FROM mission_canceled c\n" +
                "     join mission m on m.id=c.mission_id and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+"\n" +
                "     where m.driverInfo_id is null and (unix_timestamp(c.time_of_canceled))-(unix_timestamp(m.time_requesting)) between 0 and 30 and c.cancel_by not in('operator', 'driver', 'server')) as countWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes,\n" +
                "     \n" +
                "(SELECT count(*) FROM mission m\n" +
                "join region r on r.id=m.region_id\n" +
                "where m.driverInfo_id is null and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" and r.type_region=2) as countFromRemoteZone\n" +
                "\n" +
                "from mission m where (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+") as RESULT ");
        */


        LOGGER.info("Bukanov query: " + query);

        Session session = entityManager.unwrap(Session.class);


        Query hQuery = session.createSQLQuery(query.toString());


        List listValue = hQuery.list();


        /* 0 - allCount */
        /* 1 - outerCount */
        /* 2 - innerCount */
        /* 3 - doubleCount */
        /* 4 - countWithDriver */
        /* 5 - countWithDriverAndCompleted */
        /* 6 - countWithDriverAndCanceled */
        /* 7 - countWithDriverAndCanceledByClient */
        /* 8 - countWithDriverAndCanceledByDriver */
        /* 9 - countWithDriverAndCanceledByOperator */
        /* 10 - countWithoutDriver */
        /* 11 - countWithoutDriverAndCanceledWhenMissionAfterHalfMinutes */
        /* 12 - countWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch */
        /* 13 - countWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes */
        /* 14 - countFromRemoteZone */
        /* 15 - uniqueCount */
        /* 16 - countWithDriverAndCanceledByServer */


        response.getReportInfo().setAllCount(listValue.get(0) == null ? 0 : ((BigInteger) listValue.get(0)).intValue()); // +
        response.getReportInfo().setOuterCount(listValue.get(1) == null ? 0 : ((BigInteger) listValue.get(1)).intValue()); // +
        response.getReportInfo().setInnerCount(listValue.get(2) == null ? 0 : ((BigInteger) listValue.get(2)).intValue()); // +
        response.getReportInfo().setDoubleCount(listValue.get(3) == null ? 0 : ((BigInteger) listValue.get(3)).intValue()); // +
        response.getReportInfo().setCountWithDriver(listValue.get(4) == null ? 0 : ((BigInteger) listValue.get(4)).intValue()); // +
        response.getReportInfo().setCountWithDriverAndCompleted(listValue.get(5) == null ? 0 : ((BigInteger) listValue.get(5)).intValue()); // +
        response.getReportInfo().setCountWithDriverAndCanceled(listValue.get(6) == null ? 0 : ((BigInteger) listValue.get(6)).intValue()); // +
        response.getReportInfo().setCountWithDriverAndCanceledByClient(listValue.get(7) == null ? 0 : ((BigInteger) listValue.get(7)).intValue()); // +
        response.getReportInfo().setCountWithDriverAndCanceledByDriver(listValue.get(8) == null ? 0 : ((BigInteger) listValue.get(8)).intValue()); // +
        response.getReportInfo().setCountWithDriverAndCanceledByOperator(listValue.get(9) == null ? 0 : ((BigInteger) listValue.get(9)).intValue()); // +


        int countWithoutDriver = listValue.get(10) == null ? 0 : ((BigInteger) listValue.get(10)).intValue();
        int countWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch = listValue.get(12) == null ? 0 : ((BigInteger) listValue.get(12)).intValue();
        int countWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes = listValue.get(13) == null ? 0 : ((BigInteger) listValue.get(13)).intValue();
        int countFromRemoteZone = listValue.get(14) == null ? 0 : ((BigInteger) listValue.get(14)).intValue();
        response.getReportInfo().setCountWithoutDriver(countWithoutDriver);
        response.getReportInfo().setCountWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch(countWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch);
        response.getReportInfo().setCountWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes(countWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes);
        response.getReportInfo().setCountFromRemoteZone(countFromRemoteZone);

        //int countWithoutDriverAndCanceledWhenMissionAfterHalfMinutes = (countWithoutDriver - (countWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes + countWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch)) + (countFromRemoteZone+countWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch);
        response.getReportInfo().setCountWithoutDriverAndCanceledWhenMissionAfterHalfMinutes(listValue.get(11) == null ? 0 : ((BigInteger) listValue.get(11)).intValue()); // listValue.get(11) == null ? 0 : ((BigInteger) listValue.get(11)).intValue()


        response.getReportInfo().setUniqueCount(listValue.get(15) == null ? 0 : ((BigInteger) listValue.get(15)).intValue());
        response.getReportInfo().setCountWithDriverAndCanceledByServer(listValue.get(16) == null ? 0 : ((BigInteger) listValue.get(16)).intValue());

        double sum = response.getReportInfo().getCountWithoutDriver() + response.getReportInfo().getCountWithDriver();
        if (sum == 0) {
            response.getReportInfo().setMainPercent(0);
        } else {
            double res = response.getReportInfo().getCountWithoutDriver() / sum;
            response.getReportInfo().setMainPercent(res*100);
        }

        return response;
    }


    private void test(long startTime, long endTime) {
        StringBuilder query = new StringBuilder();
        query.append(
                "select count(*) as allCount from mission where (unix_timestamp(time_requesting)+21600) between " + startTime + " and " + endTime + "\n" +
                        " union all " +
                        "select count(*) as outerCount from mission where test_order=0 and (unix_timestamp(time_requesting)+21600) between " + startTime + " and " + endTime + " \n" +
                        " union all " +
                        "select count(*) as innerCount from mission where test_order=1 and (unix_timestamp(time_requesting)+21600) between " + startTime + " and " + endTime + "\n" +
                        " union all " +
                        "select sum(count) from(select * from(\n" +
                        "             select m2.from_address as fromAddress, m2.to_address as toAddress,\n" +
                        "             (select count(*)-1 from mission m1 where m1.from_address = m2.from_address and m1.to_address = m2.to_address and m1.clientInfo_id = m2.clientInfo_id and (unix_timestamp(m1.time_requesting)+21600) between " + startTime + " and " + endTime + ") as count\n" +
                        "             from mission m2 where (unix_timestamp(m2.time_requesting)+21600) between " + startTime + " and " + endTime + ") as result group by concat(result.fromAddress, result.toAddress) having count>0) as doubleCount\n" +
                        " union all " +
                        "SELECT count(*) as countWithDriver FROM mission m where m.driverInfo_id is not null and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between " + startTime + " and " + endTime + "\n" +
                        " union all " +
                        "SELECT count(*) as countWithDriverAndCompleted FROM mission m where m.driverInfo_id is not null and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between " + startTime + " and " + endTime + " and m.state='COMPLETED'\n" +
                        " union all " +
                        "SELECT count(*) as countWithDriverAndCanceled FROM mission_canceled c\n" +
                        "                   inner join mission m on m.id=c.mission_id and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between " + startTime + " and " + endTime + "\n" +
                        "                   and m.driverInfo_id is not null and (unix_timestamp(c.time_of_canceled)+21600) between " + startTime + " and " + endTime + "\n" +
                        "                   \n" +
                        "SELECT count(*) as countWithDriverAndCanceledByClient FROM mission_canceled c\n" +
                        "                   inner join mission m on m.id=c.mission_id and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between " + startTime + " and " + endTime + "\n" +
                        "                   and c.cancel_by='client' and m.driverInfo_id is not null and (unix_timestamp(c.time_of_canceled)+21600) between " + startTime + " and " + endTime + "\n" +
                        " union all " +
                        " SELECT count(*) as countWithDriverAndCanceledByDriver FROM mission_canceled c\n" +
                        "                   inner join mission m on m.id=c.mission_id and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between " + startTime + " and " + endTime + "\n" +
                        "                   and c.cancel_by='driver' and m.driverInfo_id is not null and (unix_timestamp(c.time_of_canceled)+21600) between " + startTime + " and " + endTime + " \n" +
                        " union all " +
                        "SELECT count(*) as countWithDriverAndCanceledByOperator FROM mission_canceled c\n" +
                        "                   inner join mission m on m.id=c.mission_id and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between " + startTime + " and " + endTime + "\n" +
                        "                   and c.cancel_by='operator' and m.driverInfo_id is not null and (unix_timestamp(c.time_of_canceled)+21600) between " + startTime + " and " + endTime + "\n" +
                        " union all " +
                        "SELECT count(*) as countWithoutDriver FROM mission_canceled c\n" +
                        "     join mission m on m.id=c.mission_id and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between " + startTime + " and " + endTime + "\n" +
                        "     where m.driverInfo_id is null and (unix_timestamp(c.time_of_canceled)+21600) between " + startTime + " and " + endTime + "\n" +
                        " union all " +
                        "SELECT count(*) as countWithoutDriverAndCanceledWhenMissionAfterHalfMinutes FROM mission_canceled c\n" +
                        "     join mission m on m.id=c.mission_id and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between " + startTime + " and " + endTime + "\n" +
                        "     where m.driverInfo_id is null and (unix_timestamp(c.time_of_canceled))-(unix_timestamp(m.time_requesting)) between 30 and 60 and  (unix_timestamp(c.time_of_canceled)+21600) between " + startTime + " and " + endTime + "\n" +
                        " union all " +
                        "SELECT count(*) as countWithoutDriverAndCanceledByClientWhenMissionOnAutoSearch FROM mission_canceled c\n" +
                        "     join mission m on m.id=c.mission_id and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+"\n" +
                "     where m.driverInfo_id is null and c.cancel_by='client' and c.state_before_canceled='AUTO_SEARCH' and  (unix_timestamp(c.time_of_canceled)+21600) between "+startTime+" and "+endTime+"\n" +
                " union all " +
                "SELECT count(*) as countWithoutDriverAndCanceledWhenMissionBeforeHalfMinutes FROM mission_canceled c\n" +
                "     join mission m on m.id=c.mission_id and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+"\n" +
                "     where m.driverInfo_id is null and (unix_timestamp(c.time_of_canceled))-(unix_timestamp(m.time_requesting)) between 0 and 30 and c.cancel_by not in('operator', 'driver', 'server')\n" +
                " union all " +
                "SELECT count(*) as countFromRemoteZone FROM mission m\n" +
                "join region r on r.id=m.region_id\n" +
                "where m.driverInfo_id is null and m.test_order=0 and (unix_timestamp(m.time_requesting)+21600) between "+startTime+" and "+endTime+" and r.type_region=2"
                );
    }









    @Transactional(propagation = Propagation.REQUIRED)
    public GlobalClientStatsResponse getClientGlobalStats(long registrationStart, long registrationEnd){
        GlobalClientStatsResponse response = new GlobalClientStatsResponse();

        Session session = entityManager.unwrap(Session.class);

        StringBuilder builder = new StringBuilder();
        Map parameters = new HashMap();

        builder.append("from Client c ");
        builder.append(" where 1=1 ");

        if (registrationStart != 0 && registrationEnd != 0) {
            builder.append(" and c.registrationTime >= :startDate and c.registrationTime <= :endDate");
            parameters.put("startDate", new DateTime(new Date(registrationStart * 1000)));
            parameters.put("endDate", new DateTime(new Date(registrationEnd * 1000)));
        } else if (registrationStart != 0 && registrationEnd == 0) {
            builder.append(" and c.registrationTime >= :startDate ");
            parameters.put("startDate", new DateTime(new Date(registrationStart * 1000)));
        } else if (registrationStart == 0 && registrationEnd != 0) {
            builder.append(" and c.registrationTime <= :endDate");
            parameters.put("endDate", new DateTime(new Date(registrationEnd * 1000)));
        }

        builder.append(" and c.registrationState = :registrationState");
        parameters.put("registrationState", Client.RegistrationState.CONFIRMED);
        //builder.append(" order by c.id desc");

        String hqlQuery = builder.toString();

        LOGGER.info("QUERY: "+hqlQuery);

        Query query = session.createQuery(hqlQuery);
        Iterator iter = parameters.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            Object value = parameters.get(name);
            query.setParameter(name,value);
        }

        List<Object> result = (List<Object>) query.list();

        List<Client> clients = new ArrayList<>();
        Iterator itr = result.iterator();
        while(itr.hasNext() ){
            Client client = null;
            Object object = itr.next();
            if(object instanceof Client){
                client = (Client) object;
            } else if(object instanceof Object[]){
                Object[] obj = (Object[]) object;
                client = (Client)obj[0];
            }
            if(client!=null){
                clients.add(client);
            }
        }


        int count = 0;

        for(Client client : clients){
            //LOGGER.info("client id: "+client.getId()+" count: "+count++);
            GlobalClientStatsInfo info = new GlobalClientStatsInfo();

            ClientStatsInfo shortStat = getClientStats(client.getId());

            long clientId = client.getId();

            info.setClientId(clientId);
            info.setFirstName(client.getFirstName());
            info.setLastName(client.getLastName());
            if(client.getRegistrationTime()!=null){
                info.setRegistrationDate(client.getRegistrationTime().toLocalDate().toString());
            }
            info.setCity(client.getCity());

            ClientLocks clientLocks = clientLockRepository.findByClientIdAndTimeOfUnlockIsNull(clientId);
            info.setBlockState(clientLocks != null ? "заблокирован" : "не заблокирован");
            info.setDateOfBlock(clientLocks != null ? DateTimeUtils.toDateTime(clientLocks.getTimeOfLock()).toLocalDate().toString() : "");
            info.setReasonOfBlock(clientLocks != null ? clientLocks.getReason() : "");
            info.setDateOfFirstOrder(firstOrderByClient(client));
            info.setDateOfLastOrder(lastOrderByClient(client));

            info.setCountMission(shortStat.getCountMission());
            info.setAverageCountMissionMonth(shortStat.getAverageCountMissionMonth());
            info.setAllSummMissionMoney(shortStat.getAllSummMissionMoney());
            info.setAverageCheckMoney(shortStat.getAverageCheckMoney());

            info.setCountCanceledMissionWithoutDriver(countCancelByClientWithoutDriver(clientId, session));
            info.setCountCanceledMissionWithDriver(countCancelByClientWithDriver(clientId, session));
            info.setCountLowCoster(countMissionByAutoClass(clientId, "LOW_COSTER", session));
            info.setCountStandard(countMissionByAutoClass(clientId, "STANDARD", session));
            info.setCountComfort(countMissionByAutoClass(clientId, "COMFORT", session));
            info.setCountBusiness(countMissionByAutoClass(clientId, "BUSINESS", session));
            info.setCountEstimate(countClientEstimate(clientId, session));
            info.setCountComments(countClientComments(clientId, session));
            info.setAvgRate(avgRate(clientId, session));
            info.setCountPromoSend(countPromoSend(clientId, session));
            info.setCountPromoActivated(countPromoActivated(clientId, session));
            info.setCountAutosearch(countAutosearch(clientId, session));
            info.setCountServices(countServices(clientId, session));
            info.setCountLikeMission(countLikeFromMission(clientId, session) + countLikeToMission(clientId, session));
            info.setCountEqualMission(countEqualMission(clientId, session));
            info.setCountWatchMission(countWatchMission(clientId, session));

            info.setMotive(findMotive(client));
            info.setCountCallInSupport(0);

            response.getGlobalClientStatsInfos().add(info);
        }

        return response;
    }






    public ClientStatsInfo getClientStats(long clientId){
        ClientStatsInfo clientStatsInfo = new ClientStatsInfo();
        //Client client = clientRepository.findOne(clientId);
        Session session = entityManager.unwrap(Session.class);

        //if(client!=null){
              /*
              Query query = session.createSQLQuery(
                      "select count(*) from mission where clientInfo_id = :id and state='COMPLETED'"+
                      " union "+
                      "select count(*) from mission where clientInfo_id = :id and state='CANCELED'"+
                      " union "+
                      "select count(*)/count(distinct date_format(time_finishing,'%Y-%m')) from mission where clientInfo_id = :id and state='COMPLETED' and test_order=0"+
                      " union "+
                      "SELECT count(*) from mission where clientInfo_id = :id and state='COMPLETED' and time_finishing > LAST_DAY(CURDATE()) + INTERVAL 1 DAY - INTERVAL 1 MONTH" +
                      " AND  time_finishing < DATE_ADD(LAST_DAY(CURDATE()), INTERVAL 1 DAY) and test_order=0"+
                      " union "+
                      "SELECT sum(price_in_fact_amount) from mission where clientInfo_id = :id and state='COMPLETED' and time_finishing > LAST_DAY(CURDATE()) + INTERVAL 1 DAY - INTERVAL 1 MONTH" +
                    " AND  time_finishing < DATE_ADD(LAST_DAY(CURDATE()), INTERVAL 1 DAY) and test_order=0"+
                      " union "+
                      "SELECT SUM(price_in_fact_amount) from mission where clientInfo_id = :id and state='COMPLETED'"+
                      " union "+
                      "select sum(price_in_fact_amount)/count(*) from mission where clientInfo_id = :id and state='COMPLETED'"+
                      " union "+
                      "SELECT sum(price_in_fact_amount)/count(distinct date_format(time_finishing,'%Y-%m')) from mission where clientInfo_id = :id and state='COMPLETED'"+
                      " union "+
                      "SELECT avg(price_in_fact_amount) from mission where clientInfo_id = :id and state = 'COMPLETED' and time_finishing > LAST_DAY(CURDATE()) + INTERVAL 1 DAY - INTERVAL 1 MONTH" +
                    " AND  time_finishing < DATE_ADD(LAST_DAY(CURDATE()), INTERVAL 1 DAY) and test_order=0"+
              "");
              query.setParameter("id", client.getId());
              List res = query.list();
              LOGGER.info("res = "+res);
              */

//  -----------------------------------------------------------------------------------------------------------------------------------------
            //   общее количество заказов  + //
            Query query = session.createSQLQuery("select count(*) from mission where clientInfo_id = :id and state='COMPLETED'");
            query.setParameter("id", clientId);
            BigInteger result = (BigInteger)query.uniqueResult();
            clientStatsInfo.setCountMission(result.intValue());


            //   кол-во отказов от заказов  + //
            query = session.createSQLQuery("select count(*) from mission where clientInfo_id = :id and state='CANCELED'");
            query.setParameter("id", clientId);
            result = (BigInteger)query.uniqueResult();
            clientStatsInfo.setCountCanceledMission(result.intValue());


            //  среднее кол-во заказов за месяц + //
            query = session.createSQLQuery("select count(*)/count(distinct date_format(time_finishing,'%Y-%m')) from mission where clientInfo_id = :id and state='COMPLETED' and test_order=0");
            query.setParameter("id", clientId);
            BigDecimal bigDecimal = (BigDecimal)query.uniqueResult();
            clientStatsInfo.setAverageCountMissionMonth(bigDecimal!=null ? bigDecimal.doubleValue(): 0);


            //   Кол-во за текущий месяц  + //
            query = session.createSQLQuery("SELECT count(*) from mission where clientInfo_id = :id and state='COMPLETED' and time_finishing > LAST_DAY(CURDATE()) + INTERVAL 1 DAY - INTERVAL 1 MONTH" +
                    " AND  time_finishing < DATE_ADD(LAST_DAY(CURDATE()), INTERVAL 1 DAY) and test_order=0");
            query.setParameter("id", clientId);
            result = (BigInteger)query.uniqueResult();
            clientStatsInfo.setCountMissionCurrentMonth(result.intValue());



            /* сумма в месяц (в деньгах) (текущий) + */
            query = session.createSQLQuery("SELECT sum(price_in_fact_amount) from mission where clientInfo_id = :id and state='COMPLETED' and time_finishing > LAST_DAY(CURDATE()) + INTERVAL 1 DAY - INTERVAL 1 MONTH" +
                    " AND  time_finishing < DATE_ADD(LAST_DAY(CURDATE()), INTERVAL 1 DAY) and test_order=0");
            query.setParameter("id", clientId);
            bigDecimal = (BigDecimal)query.uniqueResult();
            clientStatsInfo.setSummMissionMoneyCurrentMonth(bigDecimal!=null ? bigDecimal.doubleValue(): 0);


            /* общая сумма заказов (в деньгах) + */
            query = session.createSQLQuery("SELECT SUM(price_in_fact_amount) from mission where clientInfo_id = :id and state='COMPLETED'");
            query.setParameter("id", clientId);
            bigDecimal = (BigDecimal)query.uniqueResult();
            clientStatsInfo.setAllSummMissionMoney(bigDecimal!=null ? bigDecimal.doubleValue(): 0);


            /* средний чек вообще (в деньгах),  - сумма заказов (точно так же как и среднее кол-во в месяц) + */
            query = session.createSQLQuery("select sum(price_in_fact_amount)/count(*) from mission where clientInfo_id = :id and state='COMPLETED'");
            query.setParameter("id", clientId);
            bigDecimal = (BigDecimal)query.uniqueResult();
            clientStatsInfo.setAverageCheckMoney(bigDecimal!=null ? bigDecimal.doubleValue(): 0);


            /* средняя сумма в месяц (в деньгах) за весь период !!!!!!! + */
            query = session.createSQLQuery("SELECT sum(price_in_fact_amount)/count(distinct date_format(time_finishing,'%Y-%m')) from mission where clientInfo_id = :id and state='COMPLETED'");
            query.setParameter("id", clientId);
            bigDecimal = (BigDecimal)query.uniqueResult();
            clientStatsInfo.setAllAverageSummMissionMoneyMonth(bigDecimal!=null ? bigDecimal.doubleValue(): 0);


            query = session.createSQLQuery("SELECT avg(price_in_fact_amount) from mission where clientInfo_id = :id and state = 'COMPLETED' and time_finishing > LAST_DAY(CURDATE()) + INTERVAL 1 DAY - INTERVAL 1 MONTH" +
                    " AND  time_finishing < DATE_ADD(LAST_DAY(CURDATE()), INTERVAL 1 DAY) and test_order=0");
            query.setParameter("id", clientId);
            bigDecimal = (BigDecimal)query.uniqueResult();
            clientStatsInfo.setAverageSummMissionMoneyCurrentMonth(bigDecimal!=null ? bigDecimal.doubleValue(): 0);

        return clientStatsInfo;
    }
}
