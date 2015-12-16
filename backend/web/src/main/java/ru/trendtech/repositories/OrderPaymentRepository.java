package ru.trendtech.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.Order;
import ru.trendtech.domain.courier.OrderPayment;
import ru.trendtech.domain.courier.PaymentState;

import java.util.List;

/**
 * Created by petr on 14.09.2015.
 */
@Repository
public interface OrderPaymentRepository extends PagingAndSortingRepository<OrderPayment, Long> {
     List<OrderPayment> findByOrder(Order order);
     List<OrderPayment> findByOrderAndPaymentState(Order order, PaymentState paymentState);
     List<OrderPayment> findByOrderAndPaymentStateIn(Order order, List<PaymentState> paymentStateList);
     List<OrderPayment> findByOrderOrderByTimeOfRequestingDesc(Order order, Pageable pageable);
     OrderPayment findByTransactionId(String transactionId);

     @Query("select op from OrderPayment op where op.id = ?1")
     OrderPayment getOrderPaymentById(long orderPaymentId);
}
