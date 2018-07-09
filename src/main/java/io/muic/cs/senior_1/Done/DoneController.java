package io.muic.cs.senior_1.Done;

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

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Controller
public class DoneController {

    @Autowired
    private DoneRepository doneRepository;

    @PostMapping("/upload_done")
    public ResponseEntity uploadDone(@RequestParam String username, @RequestParam Long exercise, @RequestParam Integer score, @RequestParam Integer outof){
        DoneModel ee = doneRepository.findByUsernameAndExerciseid(username,exercise);
//        System.out.println(ee);
//        DoneModel d = new DoneModel();
//        d.setUsername(username);
//        d.setExerciseid(exercise);
//        d.setOutof(outof);
//        d.setScore(score);
//        doneRepository.save(d);
        if (ee == null){
            DoneModel d = new DoneModel();
            d.setUsername(username);
            d.setExerciseid(exercise);
            d.setOutof(outof);
            d.setScore(score);
            doneRepository.save(d);
            System.out.println("make new shit");
        }
        else {
            if (ee.getScore()<score){
                ee.setScore(score);
                ee.setOutof(outof);
                doneRepository.save(ee);
                System.out.println("update this score");
            }
            else if (!ee.getOutof().equals(outof)){
                ee.setScore(score);
                ee.setOutof(outof);
                System.out.println("full score change");
            }

            else {
                System.out.println("no update");
            }


//            System.out.println("me laew");
//            return ResponseEntity.ok("");
        }
        return ResponseEntity.ok("");
    }

    @GetMapping(path="/whole_done")
    public @ResponseBody Iterable<DoneModel> wholeDone (){
        System.out.println(" ----- Find all done ----- ");
        return doneRepository.findAll();
    }

    @GetMapping(path="/each_done")
    public @ResponseBody Iterable<DoneModel> eachDone (@RequestParam String username){
        System.out.println(" ----- Find all done ----- ");
        return doneRepository.findByUsername(username);
    }
}
