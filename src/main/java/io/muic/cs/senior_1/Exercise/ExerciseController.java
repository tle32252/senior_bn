package io.muic.cs.senior_1.Exercise;

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
public class ExerciseController {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private TopicRepository topicRepository;

    @RequestMapping(value = "/post_new_topic", method =  RequestMethod.POST)
    public ResponseEntity newQuestionNewTopic(@RequestBody ExerciseModel exer){

        ExerciseModel e = new ExerciseModel();
        TopicModel t = new TopicModel();

        e.setTopic(exer.getTopic());
        e.setRank(exer.getRank());
        e.setQuestion(exer.getQuestion());
        e.setChoiceA(exer.getChoiceA());
        e.setChoiceB(exer.getChoiceB());
        e.setChoiceC(exer.getChoiceC());
        e.setChoiceD(exer.getChoiceD());
        e.setCorrect(exer.getCorrect());

        t.setTopic(exer.getTopic());

        exerciseRepository.save(e);
        topicRepository.save(t);

        return ResponseEntity.ok("ok");

    }

    @RequestMapping(value = "/post_old_topic", method =  RequestMethod.POST)
    public ResponseEntity newQuestionOldTopic(@RequestBody ExerciseModel exer){

        ExerciseModel e = new ExerciseModel();


        e.setTopic(exer.getTopic());
        e.setQuestion(exer.getQuestion());
        e.setRank(exer.getRank());
        e.setChoiceA(exer.getChoiceA());
        e.setChoiceB(exer.getChoiceB());
        e.setChoiceC(exer.getChoiceC());
        e.setChoiceD(exer.getChoiceD());
        e.setCorrect(exer.getCorrect());

        exerciseRepository.save(e);

        return ResponseEntity.ok("ok");
    }

    @GetMapping(path="/each_topic")
    public @ResponseBody Iterable<ExerciseModel> eachTopic (@RequestParam String topic){
        return exerciseRepository.findByTopicOrderByRankAsc(topic);
    }

    @GetMapping(path="/check_null_topic")
    public @ResponseBody Iterable<ExerciseModel> check_null (@RequestParam String topic){
        Iterable<ExerciseModel> ee =  exerciseRepository.findByTopic(topic);
        System.out.println(ee.toString().length());
        return ee;
    }



    @GetMapping(path="/topic_item")
    public @ResponseBody Iterable<TopicModel> topicItem (){
        System.out.println(" ----- Find all topics ----- ");
        return topicRepository.findAll();
    }




    @RequestMapping(value = "/check_score", method =  RequestMethod.POST)
    public @ResponseBody Integer checkScore(@RequestBody List<StudentAnswerModel> studans){
        Integer mark = 0;

        System.out.println(" ----- Check Score ----- ");

        List<Long> ids = new ArrayList<>();

        for (int i =0; i < studans.size(); i++){
            ids.add(studans.get(i).getId());
        }
        Iterable<ExerciseModel> rr = exerciseRepository.findAll(ids); //convert to map
        List<ExerciseModel> target = new ArrayList<>();
        rr.forEach(target::add);

        Map<Long, String> result2 = target.stream().collect(Collectors.toMap(ExerciseModel::getId, ExerciseModel::getCorrect));

        for (int i =0; i < studans.size(); i++){

            if(studans.get(i).getAnschoose().equals(result2.get(studans.get(i).getId()))){
                mark++;
            }
        }
//        System.out.println(studans);
        return mark;
    }

    @RequestMapping(value = "/delete_topic", method =  RequestMethod.POST)
    public ResponseEntity deleteTopic (@RequestParam String topic){
        Iterable<ExerciseModel> e = exerciseRepository.findByTopic(topic);
        Iterable<TopicModel> t = topicRepository.findByTopic(topic);

        exerciseRepository.delete(e);
        topicRepository.delete(t);

        return ResponseEntity.ok("OK");
    }

    @RequestMapping(value = "/delete_null_topic", method =  RequestMethod.POST)
    public ResponseEntity deleteNullTopic (@RequestParam String topic){
//        Iterable<ExerciseModel> e = exerciseRepository.findByTopic(topic);
        Iterable<TopicModel> t = topicRepository.findByTopic(topic);

//        exerciseRepository.delete(e);
        topicRepository.delete(t);

        return ResponseEntity.ok("OK");
    }



    @RequestMapping(value = "/delete_question", method =  RequestMethod.POST)
    public   ResponseEntity deleteQuestion (@RequestParam Long id){
        ExerciseModel e = exerciseRepository.findById(id);
        exerciseRepository.delete(e);

        return ResponseEntity.ok("OK");
    }


    @RequestMapping(value = "/each_id", method =  RequestMethod.GET)
    public @ResponseBody ExerciseModel eachId (@RequestParam Long id){
        ExerciseModel e = exerciseRepository.findById(id);
//        Iterable<TopicModel> t = topicRepository.findByTopic(topic);
//        TopicModel t = topicRepository.fi
//        exerciseRepository.delete(e);
//        topicRepository.delete(t);

        return e;
    }


//    @PutMapping(path="/update_by_kitchen") // Map ONLY GET Requests
//    public @ResponseBody String addNewUser (@RequestParam Long id, @RequestParam String currentStatus) {
//        // @ResponseBody means the returned String is the response, not a view name
//        // @RequestParam means it is a parameter from the GET or POST request
//
//        Order order_1 = orderRepository.findOne(id);
//        order_1.setCurrentStatus(currentStatus);
//        orderRepository.save(order_1);
//
//        System.out.println("Update status by kitchen.");
//
//        return "Saved";
//
//    }
//
    @PutMapping(path="/update_question") // Map ONLY GET Requests
    public @ResponseBody String updateQuestion (@RequestParam Long id, @RequestBody ExerciseModel exer) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        ExerciseModel e = exerciseRepository.findById(id);


        e.setQuestion(exer.getQuestion());
        e.setRank(exer.getRank());
        e.setChoiceA(exer.getChoiceA());
        e.setChoiceB(exer.getChoiceB());
        e.setChoiceC(exer.getChoiceC());
        e.setChoiceD(exer.getChoiceD());
        e.setCorrect(exer.getCorrect());

        exerciseRepository.save(e);


        System.out.println("Update question.");

        return "Saved";

    }

}

