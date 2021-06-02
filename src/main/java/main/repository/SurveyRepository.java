package main.repository;

import main.model.Survey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends PagingAndSortingRepository<Survey, Integer> {

    @Query(value = "SELECT * FROM surveys " +
            "WHERE finish_date > now() AND start_date < now()", nativeQuery = true)
    Optional<List<Survey>> findAllActiveSurveys();
}
