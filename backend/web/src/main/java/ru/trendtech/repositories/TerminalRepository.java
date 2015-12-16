package ru.trendtech.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.trendtech.domain.Terminal;

/**
 * Created by petr on 22.10.2014.
 */
public interface TerminalRepository extends CrudRepository<Terminal, Long> {
}
