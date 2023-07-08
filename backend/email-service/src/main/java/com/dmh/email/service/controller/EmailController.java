package com.dmh.email.service.controller;

import com.dmh.email.service.model.EmailRequest;
import com.dmh.email.service.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    EmailSenderService emailSenderService;

    @PostMapping()
    public ResponseEntity<?> sendEmail( @RequestBody EmailRequest email) throws Exception {

        //valido campos
        emailSenderService.sendMail(email.getToEmail(),email.getSubject(),email.getText());

        //retorno response
        return ResponseEntity.ok("");
    }
}
