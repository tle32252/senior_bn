package io.muic.cs.senior_1.Done;

import org.springframework.data.repository.CrudRepository;

        import org.springframework.data.jpa.repository.JpaRepository;

        import org.springframework.data.repository.CrudRepository;

public interface DoneRepository extends CrudRepository<DoneModel, String> {
    DoneModel findByUsernameAndExerciseid(String username, Long Exerciseid);
    Iterable<DoneModel> findByUsername(String username);
}
