package main.repository;

import main.model.AnswerItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerItemRepository extends CrudRepository<AnswerItem, Integer> {

}
