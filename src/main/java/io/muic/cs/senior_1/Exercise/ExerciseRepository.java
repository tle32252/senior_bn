package io.muic.cs.senior_1.Exercise;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExerciseRepository extends CrudRepository<ExerciseModel, Long>{
    Iterable<ExerciseModel> findByTopicOrderByRankAsc(String topic);
    Iterable<ExerciseModel> findByTopic(String topic);
    Iterable<ExerciseModel> deleteByTopic(String topic);
    List<ExerciseModel> findByQuestion(String question);
    ExerciseModel findById(Long id);
}
