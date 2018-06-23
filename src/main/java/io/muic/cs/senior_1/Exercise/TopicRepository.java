package io.muic.cs.senior_1.Exercise;

import org.springframework.data.repository.CrudRepository;


public interface TopicRepository extends CrudRepository<TopicModel, String>{
    Iterable<TopicModel> findByTopic(String topic);
}
