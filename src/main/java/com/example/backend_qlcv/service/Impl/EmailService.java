package com.example.backend_qlcv.service.Impl;
import com.example.backend_qlcv.entity.Card;
import com.example.backend_qlcv.entity.CardAssignment;
import com.example.backend_qlcv.repository.CardAssignmentRepository;
import com.example.backend_qlcv.repository.CardRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EmailService {
    @Autowired
    private final JavaMailSender mailSender;

    @Autowired
    private final CardRepository cardRepository;

    @Autowired
    private final CardAssignmentRepository cardAssignmentRepository;

    public EmailService(JavaMailSender emailSender, CardRepository cardRepository, CardAssignmentRepository cardAssignmentRepository) {
        this.mailSender = emailSender;
        this.cardRepository = cardRepository;
        this.cardAssignmentRepository = cardAssignmentRepository;
    }


    public void sendEmail(String to, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    @Scheduled(cron = "0 0 9 * * ?")
    public void sendReminders() {
        List<Card> cards = cardRepository.findAll();

        for (Card card : cards) {
            if (isDueInOneDay(card.getDueDate())) {
                List<CardAssignment> assignments = cardAssignmentRepository.findByCard(card);
                for (CardAssignment assignment : assignments) {
                    sendEmail(assignment.getUser().getEmail(), "Task Due Reminder",
                            "Your task '" + card.getTitle() + "' is due tomorrow.");
                }
            }
        }
    }


    private boolean isDueInOneDay(Date dueDate) {
        long millisInDay = 24 * 60 * 60 * 1000;
        long currentTime = System.currentTimeMillis();
        long dueTime = dueDate.getTime();

        // Lấy số ngày tính từ epoch cho thời gian hiện tại và dueDate
        long currentDay = currentTime / millisInDay;
        long dueDay = dueTime / millisInDay;

        return dueDay - currentDay == 1;
    }
}
