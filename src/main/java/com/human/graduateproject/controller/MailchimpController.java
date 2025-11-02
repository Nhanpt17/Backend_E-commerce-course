package com.human.graduateproject.controller;

import com.human.graduateproject.services.MailchimpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mailchimp")
@CrossOrigin(origins = "*") // Cho phép Angular FE gọi sang
public class MailchimpController {

    @Autowired
    private MailchimpService mailchimpService;

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam String email) {
        return mailchimpService.addSubscriber(email);
    }
}
