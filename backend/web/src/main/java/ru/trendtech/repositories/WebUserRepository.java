package ru.trendtech.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.domain.admin.WebUser;

import java.util.List;

@Repository
public interface WebUserRepository  extends JpaRepository<WebUser, Long> {
    List<WebUser> findByEmailAndPassword(String email, String password);
    WebUser findByToken(String token);
}
