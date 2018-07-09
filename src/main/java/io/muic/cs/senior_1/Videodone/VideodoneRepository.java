package io.muic.cs.senior_1.Videodone;
import org.springframework.data.repository.CrudRepository;

public interface VideodoneRepository extends CrudRepository<VideodoneModel,String>{
    VideodoneModel findByUsernameAndVideo(String username, String Video);
    Iterable<VideodoneModel> findByUsername(String username);
}
