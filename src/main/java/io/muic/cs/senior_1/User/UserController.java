package io.muic.cs.senior_1.User;
//package com.nimbusds.jose;

import co.omise.Client;
import co.omise.ClientException;
import co.omise.models.Charge;
import co.omise.models.ChargeStatus;
import co.omise.models.OmiseException;
import com.google.gson.Gson;
import java.security.SecureRandom;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.bouncycastle.crypto.tls.SignatureAlgorithm;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.Object.*;
import java.security.Principal;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.asser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//@CrossOrigin(origins = "http://localhost:3000")

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/whoami")
    public String whoami(Principal principal){
        return principal.getName();
    }

//    @PostMapping(path = "/fucktie")
//    public String fucktie () {
//        return "fucktie";
//    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestParam("username") String username, @RequestParam("password") String password,@RequestParam("repeatPassword") String repeatPass, @RequestParam("role") String role, @RequestParam("name") String name, @RequestParam("line") String line, @RequestParam("email") String email, @RequestParam("status") String status){
        UserModel model;

        if ((model = userService.register(username, password, repeatPass, role, name, line, email, status)) != null){
            return ResponseEntity.ok(model);
        }
        System.out.println();

        return ResponseEntity.badRequest().body("Cant Register");
    }
    

    @PostMapping("/omiseCharge")
    public @ResponseBody String chargee  (@RequestParam String omisetoken, @RequestParam String username) throws OmiseException, ClientException{
        Client client = new Client("pkey_test_5brcmnpnbk98pwnizcv", "skey_test_5brcmnpnwigil7ivmb6");
        try {
            Charge charge = client.charges().create(new Charge.Create()
                    .amount(100000) // THB 1,000.00
                    .currency("THB")
                    .card(omisetoken));
            System.out.println("created charge: " + charge.getStatus());
            if (charge.getStatus() == ChargeStatus.Successful){
                UserModel e = userRepository.findByUsername(username);
                e.setStatus("Paid");
                userRepository.save(e);

                System.out.println("Make Paid.");

                return "OK";
            }
            else {
                return "BAD";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "BAD";
    }


    @PostMapping(path="/test")
    public void  test (){
        System.out.println(" ----- TESTTT ----- ");
    }


    @GetMapping(path="/all_student")
//    @Transactional
    public @ResponseBody Iterable<UserModel> topicItem (){
        System.out.println(" ----- Find all students ----- ");
        return userRepository.findAll();
    }


    @PutMapping(path="/make_paid") // Map ONLY GET Requests
    public @ResponseBody String updateQuestion (@RequestParam Long id) {
        UserModel e = userRepository.findById(id);

        e.setStatus("paid");

        userRepository.save(e);

        System.out.println("Update question.");

        return "Saved";

    }

    @GetMapping(path="/each_student_login") // Map ONLY GET Requests
    public @ResponseBody UserModel each_stusent_by_id (@RequestParam String username) {
        UserModel e = userRepository.findByUsername(username);

//        e.setStatus("paid");

//        userRepository.save(e);

        System.out.println("each student by username");

        return e;

    }

    @GetMapping(path="/check_paid_3") // Map ONLY GET Requests
    public ResponseEntity checkPaid_3 (HttpServletRequest httpServletRequest) {
        String e = httpServletRequest.toString();
        String s = httpServletRequest.getHeader("Authorization");
        System.out.println(s);
        String q = httpServletRequest.getHeader("X-Original-URI");
        String r = httpServletRequest.getHeader("X-Original-Method");
        String v = httpServletRequest.getMethod();
        if (r.equals("GET")){
            if (s.equals("Bearer aaa")){
//                System.out.println("Good");
                return ResponseEntity.ok("");
            }
            else {
//                System.out.println("Bad");
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
        }
        return ResponseEntity.ok("");
    }

    @GetMapping(path="/decode") // Map ONLY GET Requests
    public @ResponseBody String decoded (HttpServletRequest httpServletRequest) throws JOSEException, ParseException {
        String autho = httpServletRequest.getHeader("Authorization");
        String uri = httpServletRequest.getHeader("X-Original-URI");
        String method = httpServletRequest.getHeader("X-Original-Method");

        JWSObject jwsObject = JWSObject.parse(autho);
        JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
        assertTrue(jwsObject.verify(verifier));
        String decode = jwsObject.getPayload().toString();
        return decode;
    }

    @GetMapping(path="/decode_jwt") // Map ONLY GET Requests
    public ResponseEntity decode (HttpServletRequest httpServletRequest) throws JOSEException, ParseException, IOException {
        String s = httpServletRequest.getHeader("Authorization");
        String q = httpServletRequest.getHeader("X-Original-URI");
        String r = httpServletRequest.getHeader("X-Original-Method");

//        System.out.println(s);
        if (r.equals("GET")){
            JWSObject jwsObject = JWSObject.parse(s);
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
            assertTrue(jwsObject.verify(verifier));
            ObjectMapper mapper = new ObjectMapper();
            String value = jwsObject.getPayload().toJSONObject().toJSONString();

            Map<String, Object> map = new HashMap<String, Object>();

            // convert JSON string to Map
            map = mapper.readValue(value, new TypeReference<Map<String, String>>(){});
            String nameJwt = map.get("name").toString();
            String timeJwt = map.get("expire").toString();
            int timeFromToken = Integer.parseInt(timeJwt);
            String url = map.get("video").toString();
            String[] parts = url.split("/");
            String[] partss = q.split("/");
            String part1 = parts[4]; // 004
            String part2 = partss[2];

//            System.out.println(timeJwt);
            System.out.println("url from jwt : "+part1);
            System.out.println("From header : "+part2);



            UserModel e = userRepository.findByUsername(nameJwt);
            if (e.getStatus().equals("Paid")){
                Date currentDate = new Date();
                Long ee  = currentDate.getTime()/1000;
                String yy = ee.toString();
                int currentTime = Integer.parseInt(yy);
                int result = currentTime-timeFromToken;
//                return ResponseEntity.ok("OK");
                if (result < 600){
                    System.out.println(result);
//                    return ResponseEntity.ok("OK");
                    if (part1.equals(part2)){
                        return ResponseEntity.ok("OK");
                    }
                    else{
                        return new ResponseEntity(HttpStatus.FORBIDDEN);
                    }
                }
                else {
                    System.out.println("Token Expired");
                    return new ResponseEntity(HttpStatus.FORBIDDEN);
                }
            }
            else {
                System.out.println("Very Bad");
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }

        }
        return ResponseEntity.ok("");

//        if (e.getStatus().equals("Paid")){
//            return "Treu";
//        }
//        else {
//            return "False";



//        System.out.println(map.get("name"));
//        value = value.substring(1, value.length()-1);           //remove curly brackets
//        String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs
//        Map<String,String> map = new HashMap<>();
//
//        for(String pair : keyValuePairs)                        //iterate over the pairs
//        {
//            String[] entry = pair.split("=");                   //split the pairs to get key and value
//            map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
//        }
//
//        System.out.println(map);


//        return value;
    }



    @GetMapping(path="/check_paid_1") // Map ONLY GET Requests
    public @ResponseBody Boolean checkPaid (@RequestParam String username) {
        UserModel e = userRepository.findByUsername(username);
        if (e.getStatus().equals("Paid")){
            return Boolean.TRUE;
        }
        else {
            return Boolean.FALSE;
        }

    }

    @GetMapping(path="/check_paid_2") // Map ONLY GET Requests
    public ResponseEntity checkPaid_2 (@RequestParam String username) {
        UserModel e = userRepository.findByUsername(username);
        if (e.getStatus().equals("Paid")){
            return ResponseEntity.ok("OKOK");
        }
        else {
            return ResponseEntity.badRequest().body("bad");
        }
    }

    @Value("${senior.secret:aaa}")
    private String secretKey;

    @PostMapping(path="/make_jwt") // Map ONLY GET Requests
    public @ResponseBody String makeJwt (@RequestParam String username,@RequestParam String video) throws JOSEException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date currentDate = new Date();
        Long ee  = currentDate.getTime()/1000;
        String yy = ee.toString();
        cal.add(Calendar.HOUR_OF_DAY, 1);
//        System.out.println( sdf.format(cal.getTime()).toString() );
        Map<String, String> payload = new TreeMap<>();
        payload.put("name", username);
        payload.put("video", video);
        payload.put("expire", yy);

        JSONObject payloadJson =  new JSONObject(payload);

        byte[] sharedSecret = secretKey.getBytes();

        JWSSigner signer = null;
        try {
            signer = new MACSigner(sharedSecret);
        } catch (KeyLengthException e) {
            e.printStackTrace();
        }
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(payloadJson.toString()));
        jwsObject.sign(signer);
        String s = jwsObject.serialize();
//        System.out.println(s);

        return s;
    }






    public static String serialize(Object object) throws IOException {
        ByteArrayOutputStream byteaOut = new ByteArrayOutputStream();
        GZIPOutputStream gzipOut = null;
        try {
            gzipOut = new GZIPOutputStream(new Base64OutputStream(byteaOut));
            gzipOut.write(new Gson().toJson(object).getBytes("UTF-8"));
        } finally {
            if (gzipOut != null) try { gzipOut.close(); } catch (IOException logOrIgnore) {}
        }
        return new String(byteaOut.toByteArray());
    }

    public static <T> T deserialize(String string, Type type) throws IOException {
        ByteArrayOutputStream byteaOut = new ByteArrayOutputStream();
        GZIPInputStream gzipIn = null;
        try {
            gzipIn = new GZIPInputStream(new Base64InputStream(new ByteArrayInputStream(string.getBytes("UTF-8"))));
            for (int data; (data = gzipIn.read()) > -1;) {
                byteaOut.write(data);
            }
        } finally {
            if (gzipIn != null) try { gzipIn.close(); } catch (IOException logOrIgnore) {}
        }
        return new Gson().fromJson(new String(byteaOut.toByteArray()), type);
    }
}
