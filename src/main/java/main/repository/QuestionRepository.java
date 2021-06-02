package main.repository;

import main.model.Question;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends PagingAndSortingRepository<Question, Integer> {

}
