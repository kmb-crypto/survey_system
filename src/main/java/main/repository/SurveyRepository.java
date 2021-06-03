package main.repository;

import main.dto.SurveysCompletedByUserNative;
import main.model.Survey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends PagingAndSortingRepository<Survey, Integer> {

    @Query(value = "SELECT * FROM surveys " +
            "WHERE finish_date > now() AND start_date < now()", nativeQuery = true)
    Optional<List<Survey>> findAllActiveSurveys();

    @Query(value = "SELECT DISTINCT surveys.id, surveys.title FROM surveys " +
            "JOIN questions ON surveys.id = questions.survey_id " +
            "JOIN answers ON questions.id = answers.question_id " +
            "where answers.user_id = :id", nativeQuery = true)
    List<SurveysCompletedByUserNative> findAllSurveysCompletedByUser(@Param("id") final int id);
}
