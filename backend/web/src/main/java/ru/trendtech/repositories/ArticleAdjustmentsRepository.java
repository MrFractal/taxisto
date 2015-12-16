package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.ArticleAdjustments;

/**
 * Created by petr on 12.02.2015.
 */
@Repository
public interface ArticleAdjustmentsRepository extends CrudRepository<ArticleAdjustments,Long>{
}
