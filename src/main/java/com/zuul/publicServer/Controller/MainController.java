package com.zuul.publicServer.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @GetMapping(value = "/forward")
    public ResponseEntity<String> forwardToMe(HttpServletRequest httpServletRequest){
        if(!isPresent(httpServletRequest.getHeader("Authorization"))){
            return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<String>(HttpStatus.ACCEPTED);
    }

    private boolean isPresent(String authorization) {
        if(authorization==null || authorization.isEmpty())
        {
            return false;
        }
        return true;
    }
}
