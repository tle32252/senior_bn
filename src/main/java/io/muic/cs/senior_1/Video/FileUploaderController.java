package io.muic.cs.senior_1.Video;

import io.muic.cs.senior_1.StorageProperties;
import io.muic.cs.senior_1.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/video")
public class FileUploaderController {

    private StorageService storageService;

    @Autowired
    private StorageProperties storageProperties;
    @Autowired
    private VideoRepository videoRepository;

    // @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/upload")
    public ResponseEntity uploadWithParam(@RequestParam MultipartFile file, @RequestParam String topic, @RequestParam String description){

        System.out.println(String.format("File Size: " + file.getSize()));
        System.out.println(String.format("File Name: %s", file.getOriginalFilename()));
        System.out.println(String.format("Topic: %s", topic));
        System.out.println(String.format("Description: %s", description));
        System.out.println(new File(".").toPath().toString());

        VideoModel n = new VideoModel();
        n.setTopic(topic);
        n.setUrl("http://localhost:8085/hls/"+file.getOriginalFilename()+"/index.m3u8");
        n.setDescription(description);
//        n.se(categoryType);
        n.setFilepath(file.getOriginalFilename());
//        n.setActive(Boolean.TRUE);

        videoRepository.save(n);

        try { 
            file.transferTo(Paths.get(storageProperties.getLocation(), file.getOriginalFilename()).toFile()); //new File("/Users/tleetanyaluck/" + file.getName())
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/image/**")
    public ResponseEntity<? extends Object> getImage(HttpServletRequest request){
        String[] parts = request.getServletPath().split("/");
        String name = parts[3];
        File img = Paths.get(storageProperties.getLocation(), name).toFile();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        try {

            byte[]out = org.apache.commons.io.FileUtils.readFileToByteArray(img);


            ResponseEntity respEntity = new ResponseEntity(out, headers, HttpStatus.OK);

            return respEntity;

        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
//    @GetMapping("/findall")
//    @ResponseBody
//    public Iterable<Menu> findall(){
//        return menuRepository.findAll();
//    }
//
//    @GetMapping(path="/each_kitchen")
//    public @ResponseBody Iterable<Menu> eachKitchen (@RequestParam CategoryType categoryType) {
//        // This returns a JSON or XML with the users
//        System.out.println("get each kitchen");
//
//        return menuRepository.findAllByCategoryTypeAndActive(categoryType, Boolean.TRUE);
//    }
//
//    @PutMapping(path="/del_menu") // Map ONLY GET Requests
//    public @ResponseBody String delMenu (@RequestParam Long id) {
//        // @ResponseBody means the returned String is the response, not a view name
//        // @RequestParam means it is a parameter from the GET or POST request
//        Menu menu_1 = menuRepository.findOne(id);
//        menu_1.setActive(Boolean.FALSE);
//        menuRepository.save(menu_1);
//        System.out.println("Delete menu");
//        return "Saved";
//
//    }

}
