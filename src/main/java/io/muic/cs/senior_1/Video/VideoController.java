package io.muic.cs.senior_1.Video;

//import io.muic.cs.senior_1.Exercise.TopicModel;
//import io.muic.cs.senior_1.Exercise.TopicRepository;
//import io.muic.cs.senior_1.StorageProperties;
//import io.muic.cs.senior_1.StorageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;



@Controller
public class VideoController {

    @Autowired
    private VideoRepository videoRepository;

    @GetMapping(path="/video_item")
    public @ResponseBody Iterable<VideoModel> videoItem (){
        System.out.println(" ----- Find all vids ----- ");
        return videoRepository.findAll();
    }

    @RequestMapping(value = "/delete_video", method =  RequestMethod.POST)
    public  ResponseEntity deleteVideo (@RequestParam Long id){
        VideoModel e = videoRepository.findById(id);
        videoRepository.delete(e);

        return ResponseEntity.ok("OK");
    }


}
