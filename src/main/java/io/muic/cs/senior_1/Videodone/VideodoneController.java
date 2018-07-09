package io.muic.cs.senior_1.Videodone;

import io.muic.cs.senior_1.Video.VideoModel;
import io.muic.cs.senior_1.Video.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class VideodoneController {
    @Autowired
    private VideodoneRepository videodoneRepository;

    @PostMapping("/upload_videodone")
    public ResponseEntity uploadVidDone(@RequestParam String username, @RequestParam String video){
        VideodoneModel ee = videodoneRepository.findByUsernameAndVideo(username,video);
        if (ee == null){
            VideodoneModel d = new VideodoneModel();
            d.setUsername(username);
            d.setVideo(video);
            d.setRemark(1);
            videodoneRepository.save(d);
        }
        return ResponseEntity.ok("");
    }
    @GetMapping(path="/each_videodone")
    public @ResponseBody Iterable<VideodoneModel> eachDone (@RequestParam String username){
        System.out.println(" ----- Find all done ----- ");
        return videodoneRepository.findByUsername(username);
    }
}

