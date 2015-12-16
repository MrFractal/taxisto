package ru.trendtech.repositories.courier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.OrderItemPrice;

import java.util.List;

/**
 * Created by petr on 26.08.2015.
 */
@Repository
public interface OrderItemPriceRepository extends PagingAndSortingRepository<OrderItemPrice, Long> {
    /*
    SELECT * FROM c_order_item_price oip
             join c_order ord on ord.id=oip.c_order_id
             join c_item_price ip on ip.id=oip.c_item_price_id
             where ord.client_id= 292
             group by ip.c_item_id order by oip.id asc limit 0,100
     */


    /*
       old:
       SELECT * FROM c_order_item_price oip " +
            " join c_order ord on ord.id=oip.c_order_id " +
            " where ord.client_id=?1 " +
            " group by oip.c_item_price_id order by oip.id asc limit 0,100
     */

    @Query(value = "SELECT * FROM c_order_item_price oip " +
            "             join c_order ord on ord.id = oip.c_order_id " +
            "             join c_item_price ip on ip.id = oip.c_item_price_id " +
            "             where ord.client_id= ?1 " +
            "             group by ip.c_item_id order by oip.id asc limit 0,100", nativeQuery = true)
    List<OrderItemPrice> findOrderItemPriceHistory(long clientId);

    // org.springframework.data.domain.Pageable pageable
}
