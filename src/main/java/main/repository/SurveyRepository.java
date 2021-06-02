package main.repository;

import main.model.Survey;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends PagingAndSortingRepository<Survey, Integer> {
}
