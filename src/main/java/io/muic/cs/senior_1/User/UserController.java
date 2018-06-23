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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.Principal;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
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

    @PostMapping(path="/make_jwt") // Map ONLY GET Requests
    public @ResponseBody String makeJwt (@RequestParam("username") String username,@RequestParam("video") String video) throws JOSEException {
        String key = "tttt";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        cal.add(Calendar.HOUR_OF_DAY, 1);
//        System.out.println( sdf.format(cal.getTime()).toString() );
        Map<String, String> payload = new TreeMap<>();
        payload.put("name", username);
        payload.put("video", video);
        payload.put("expire", sdf.format(cal.getTime()).toString());

        JSONObject payloadJson =  new JSONObject(payload);

        SecureRandom random = new SecureRandom();
        byte[] sharedSecret = new byte[32];
        random.nextBytes(sharedSecret);

        JWSSigner signer = null;
        try {
            signer = new MACSigner(sharedSecret);
        } catch (KeyLengthException e) {
            e.printStackTrace();
        }
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(payloadJson.toString()));
        jwsObject.sign(signer);
        String s = jwsObject.serialize();



//        String headerString = header.toString();
//        String payloadString = payload.toString();

        //JSONObject headerJson =  new JSONObject(header);


        //convet to payloadJsonStr

        //String encodedHeader = Base64.getEncoder().encodeToString(headerString.getBytes());
//        String encodedPayload = Base64.getEncoder().encodeToString(payloadString.getBytes());
        //String data = encodedHeader+"."+encodedPayload;

        //System.out.println(encodedHeader);
//        System.out.println(encodedPayload);


//        byte[] decodedBytes = Base64.getDecoder().decode(encodedHeader);
//        String decodedString = new String(decodedBytes);

//        System.out.println(decodedString);

//        JSONObject jso = new JSONObject( header );
//        String encoded = new String(Base64.Encoder( jso.toString( 4 ).toByteArray()));

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

