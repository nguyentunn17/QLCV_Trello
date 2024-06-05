package com.example.backend_qlcv.controller;


import com.example.backend_qlcv.service.Impl.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("send-reminders")
    public String sendReminders() {
        emailService.sendReminders();
        return "Reminders sent!";
    }
}
