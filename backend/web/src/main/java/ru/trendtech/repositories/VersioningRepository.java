package ru.trendtech.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.trendtech.services.versioning.DatabaseSystemInfo;

@Repository
public interface VersioningRepository extends JpaRepository<DatabaseSystemInfo, Long> {
}
