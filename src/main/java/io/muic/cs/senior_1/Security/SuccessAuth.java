package io.muic.cs.senior_1.Security;

import io.muic.cs.senior_1.User.UserModel;
import io.muic.cs.senior_1.User.UserRepository;
import org.apache.commons.io.IOUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//@CrossOrigin(origins = "http://localhost:3000")
@Component
public class SuccessAuth extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String role = authentication.getPrincipal().toString();
        String id = authentication.getClass().toString();
        IOUtils.write(new ResponseLogin("Log in successfully", true, role, id).toString(), response.getWriter());
        System.out.println("Succes auth");
    }
}