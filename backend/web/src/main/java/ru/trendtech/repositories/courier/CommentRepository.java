package ru.trendtech.repositories.courier;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.courier.Comment;
import ru.trendtech.domain.courier.Order;

/**
 * Created by petr on 26.08.2015.
 */
@Repository
public interface CommentRepository extends CrudRepository<Comment, Long>{
    Comment findByOrder(Order order);
}
