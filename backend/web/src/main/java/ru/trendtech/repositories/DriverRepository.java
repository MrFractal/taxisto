package ru.trendtech.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.*;

import java.util.List;

/**
 * Created by max on 06.02.14.
 */

@Repository
public interface DriverRepository extends PagingAndSortingRepository<Driver, Long> {
    Driver findByLoginAndPassword(String login, String password);
    List<Driver> findByTabletIsNotNullAndTabletOwn(boolean own);
    List<Driver> findByState(Driver.State state);
    Driver findByTablet(Tablet tablet);
    Driver findByRouter(Router router);
    List<Driver> findByStateAndCurrentMissionIsNull(Driver.State state);
    List<Driver> findByTypeXAndCurrentMissionIsNull(boolean typeX); // typeX = true - fantom
    List<Driver> findByTypeXAndStateNotAndCurrentMissionIsNull(boolean typeX, Driver.State state); // typeX = true - fantom
    List<Driver> findByTypeXOrderByStateAsc(boolean typeX);
    Driver findByTypeXAndIdOrderByStateAsc(boolean typeX, long driverId);
    List<Driver> findByTypeXAndCurrentMission(boolean typeX, Mission mission);
    List<Driver> findByTaxoparkPartners(TaxoparkPartners taxoparkPartners);
    Driver findByIdAndTaxoparkPartnersId(long driverId, long taxoparkId);
    List<Driver> findByStateIn(List<Driver.State> driverStates);
    List<Driver> findByStateAndCity(Driver.State state, String city);
    Driver findByLogin(String login);
    List<Driver> findByAssistant(Assistant assistant);
    Driver findByEmail(String email);
    Driver findByPhone(String phone);
    List<Driver> findByFirstNameLikeOrPhoneLikeOrAutoModelLikeOrAutoNumberLike(String nameMask, String phoneMask, String carModelMask, String carNumberMask, Pageable pageable);
    List<Driver> findByFirstNameContainingAndPhoneContainingAndAutoModelContainingAndAutoNumberContaining(String nameMask, String phoneMask, String carModelMask, String carNumberMask, Pageable pageable);
    List<Driver> findByPhoneContainingAndFirstNameContainingAndAutoModelContainingAndAutoNumberContaining(String phoneMask, String nameMask, Pageable pageable);
    List<Driver> findByAutoModelContaining(String carModelMask, Pageable pageable);
    Page<Driver> findAll(Pageable pageable);
    Driver findByToken(String token);


//    @Query(value = "select \n" +
//            "m.id,\n" +
//            "DATE_FORMAT(m.time_requesting, '%d.%m') as dateO,\n" +
//            "m.time_requesting,\n" +
//            "DATE_FORMAT(m.time_requesting, '%H.%i') as timeO, m.sum_increase, m.price_in_fact_amount,\n" +
//            "'mission' as typeR, m.from_address as address from mission m\n" +
//            "join mission_addresses ma on ma.mission_id=m.id\n" +
//            "where state='COMPLETED'  and driverInfo_id=?1 \n" +
//            "union \n" +
//            "select c_ord.id, DATE_FORMAT(c_ord.time_finishing, '%d.%m'), \n" +
//            "c_ord.time_finishing, \n" +
//            "DATE_FORMAT(c_ord.time_finishing, '%H.%i'), '0', \n" +
//            "ROUND(c_ord.price_in_fact/100, 2), \n" +
//            "'order' as typeR,\n" +
//            "(select coAddr.address from c_order_address coAddr where coAddr.c_order_id=c_ord.id order by coAddr.id desc limit 0,1) as address\n" +
//            "from c_order c_ord\n" +
//            "where c_ord.state in('COMPLETED')  and c_ord.driver_id=?1", nativeQuery = true)


    /*
    @Query(value = "select m.id, DATE_FORMAT(m.time_requesting, '%d.%m') as dateO, m.time_requesting, DATE_FORMAT(m.time_requesting, '%H.%i') as timeO, m.sum_increase, m.price_in_fact_amount,\n" +
            "    'mission' as typeR, m.from_address as address from mission m where state='COMPLETED'  and driverInfo_id=40 union\n" +
            "    select c_ord.id, DATE_FORMAT(c_ord.time_finishing, '%d.%m'), c_ord.time_finishing, DATE_FORMAT(c_ord.time_finishing, '%H.%i'), '0', ROUND(c_ord.price_in_fact/100, 2),\n" +
            "    'order' as typeR, (select coAddr.address from c_order_address coAddr where coAddr.c_order_id=c_ord.id order by coAddr.id desc limit 0,1) as address from c_order c_ord\n" +
            "    where c_ord.state='COMPLETED'  and c_ord.driver_id=40 order by time_requesting desc" , nativeQuery = true)
    List getCommonTripHistory(long driverId);
    */
}



