package app.trackwizz.connect.repository;

import app.trackwizz.connect.model.ApiCallLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiCallLogRepository extends JpaRepository<ApiCallLog, Long> {

}
