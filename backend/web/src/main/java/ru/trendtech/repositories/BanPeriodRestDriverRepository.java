package ru.trendtech.repositories;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.BanPeriodRestDriver;

import java.util.List;

/**
 * Created by petr on 28.01.2015.
 */
@Repository
public interface BanPeriodRestDriverRepository extends CrudRepository<BanPeriodRestDriver, Long>{
    /* может ли быть наложение периодов бана? если да, тогда работаем со списком, если со списком, тогда какой элемент из списка брать, первый попавшийся? (ближайший...??)*/
    Page<BanPeriodRestDriver> findByTimeOfStartingBeforeAndTimeOfEndingAfterAndActive(DateTime d1, DateTime d2, boolean active, Pageable pageable);
}
