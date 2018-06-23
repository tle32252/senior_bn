package io.muic.cs.senior_1.Video;

//import io.muic.ooc.pos.Order.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VideoRepository extends CrudRepository<VideoModel, Long>{
//    List<Menu> findAllByCategoryTypeAndActive(CategoryType categoryType, Boolean Input);
}
