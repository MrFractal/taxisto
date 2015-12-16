package ru.trendtech.repositories;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.Client;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.Mission;
import ru.trendtech.domain.billing.PaymentType;

import java.util.List;

/**
 * Created by max on 09.02.14.
 */
@Repository
public interface MissionRepository extends PagingAndSortingRepository<Mission, Long> {//CrudRepository<Mission, Long> {
    List<Mission> findByState(Mission.State state);

    @Query("select m from Mission m where m.state= ?1 and m.score.general!=0")
    List<Mission> findByStateAndGeneral(Mission.State state);

//    @Query("select count(m) from Mission m")
//    public long findCountMission();

    Mission findByIdAndState(long id,Mission.State state);

    List<Mission> findByClientInfoAndState(Client client, Mission.State state);

    @Query("select count(m) from Mission m where m.clientInfo = ?1 and m.state = ?2")
    int findCountClientByMissionState(Client client, Mission.State state);

    @Query("select m from Mission m where m.id = ?1 and m.terminal.id= ?2")
    Mission findByMissionAndTerminal(long missionId, long terminalId);

    Mission findByIdAndDriverInfoTaxoparkPartnersId(long driverId, long taxoparkId);

    List<Mission> findByClientInfoAndStateIn(Client client, List<Mission.State> stateList);

    List<Mission> findByClientInfoAndStateOrderByTimeOfStartingDesc(Client client, Mission.State state, org.springframework.data.domain.Pageable pageable); // f: add

    List<Mission> findByClientInfoAndStateOrderByTimeOfStartingDesc(Client client, Mission.State state); // f: add

    List<Mission> findByClientInfoOrderByTimeOfRequestingDesc(Client client);

    List<Mission> findByClientInfoAndStateAndStatusDeleteOrderByTimeOfStartingDesc(Client client, Mission.State state, boolean statusDelete); // f: add

    List<Mission> findByClientInfoAndStateAndStatusDeleteOrderByTimeOfStartingDesc(Client client, Mission.State state, boolean statusDelete, org.springframework.data.domain.Pageable pageable);

    List<Mission> findByClientInfoAndStateNotOrderByTimeOfStartingDesc(Client client, Mission.State state); // f: add

    List<Mission> findByDriverInfoAndState(Driver driver, Mission.State state);// f: add

    List<Mission> findByDriverInfoAndStateOrderByTimeOfStartingDesc(Driver client, Mission.State state); /// f: add, org.springframework.data.domain.Pageable pageable

    List<Mission> findByDriverInfoAndStateOrderByTimeOfStartingDesc(Driver client, Mission.State state, org.springframework.data.domain.Pageable pageable);

    List<Mission> findByClientInfo(Client client);

    List<Mission> findByDriverInfo(Driver driver);

    List<Mission> findByStateAndBookingStateAndDriverInfoIsNullOrderByTimeOfStartingAsc(Mission.State state, Mission.BookingState bookingState);

    List<Mission> findByStateAndBookingStateAndIsBookedOrderByTimeOfStartingAsc(Mission.State state, Mission.BookingState bookingState, boolean isBooked);

    List<Mission> findByStateAndBookedDriverIdOrderByTimeOfStartingAsc(Mission.State state, Driver driver);

    List<Mission> findByStateAndBookingStateNotAndBookedDriverIdOrderByTimeOfStartingAsc(Mission.State state, Mission.BookingState bookingState, Driver driver);

    //List<Mission> findByStateAndBookingStateAndDriverInfoOrderByTimeOfStartingAsc(Mission.State state, Mission.BookingState bookingState,Driver driver);

    List<Mission> findByBookingState(Mission.BookingState waiting);

    List<Mission> findByClientInfoOrderByIdDesc(Client client);

    List<Mission> findByDriverInfoOrderByIdDesc(Driver driver);

    List<Mission> findByClientInfoOrderByTimeOfStartingDesc(Client client); // f:add

    List<Mission> findByClientInfoAndTimeOfStartingBetweenOrderByTimeOfStartingDesc(Client client, DateTime start, DateTime end, org.springframework.data.domain.Pageable pageable); // f:add

    List<Mission> findByStateAndTimeOfRequestingBetween(Mission.State state, DateTime start, DateTime end); // f:add

    @Query("SELECT count(m) FROM Mission m WHERE m.driverInfo.id=?1 and m.state=?2 and m.timeOfRequesting between ?3 and ?4")
    int countCompletedMissionByDriver(long driverId, Mission.State state, DateTime dateStart, DateTime dateEnd);

    List<Mission> findByDriverInfoOrderByTimeOfRequestingDesc(Driver driver); // f:add

    List<Mission> findByDriverInfoOrderByTimeOfStartingDesc(Driver driver); // f:add


//    @Query("select count(m) from Mission m where m.clientInfo = ?1 and m.state in(Mission.State.COMPLETED, Mission.State.BOOKED)")
//    public long findCountByClientInfo(Client client);

    List<Mission> findByStateOrderByTimeOfStartingAsc(Mission.State state); // f:add
    List<Mission> findByStateAndDriverInfoTypeXOrderByTimeOfStartingAsc(Mission.State state, boolean typeX);

    List<Mission> findByStateAndPaymentTypeNotInOrderByTimeOfStartingAsc(Mission.State state, List<PaymentType> paymentTypes);

    List<Mission> findByStateAndPaymentTypeNotInAndDriverInfoTypeXOrderByTimeOfStartingAsc(Mission.State state, List<PaymentType> paymentTypes, boolean typeX);

    Page<Mission> findAll(org.springframework.data.domain.Pageable pageable);


}
