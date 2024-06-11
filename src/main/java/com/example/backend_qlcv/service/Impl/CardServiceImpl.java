package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.*;
import com.example.backend_qlcv.event.CommentEvent;
import com.example.backend_qlcv.repository.CardAssignmentRepository;
import com.example.backend_qlcv.repository.CardRepository;
import com.example.backend_qlcv.repository.ListRepository;
import com.example.backend_qlcv.repository.UserRepository;
import com.example.backend_qlcv.service.CardService;
import com.example.backend_qlcv.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListRepository listRepository;

    @Autowired
    private CardAssignmentRepository cardAssignmentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private HistoryService historyService;

    /// Lấy thời gian hiện tại và tạo thành Timestamp
    Timestamp currentTime = new Timestamp(System.currentTimeMillis());

    @Override
    public List<Card> getAll() {
        return cardRepository.findAll();
    }

    @Override
    public Card add(Card card) {
        Card cardSave = Card.builder()
                .title(card.getTitle())
                .description(card.getDescription())
                .dueDate(card.getDueDate())
                .position(card.getPosition())
                .createdAt(currentTime)
                .status(card.getStatus())
                .createBy(userRepository.findById(getIdUser(String.valueOf(card.getCreateBy()))).get())
                .lists(listRepository.findById(getIdList(String.valueOf(card.getLists()))).get())
                .build();
        cardRepository.save(cardSave);
        return null;
    }

    @Override
    public Card update(Card card, Long id) {
        Optional<Card> optionalCard = cardRepository.findById(id);
        if (optionalCard.isPresent()) {
            optionalCard.map(cardUpdate -> {
                cardUpdate.setTitle(card.getTitle());
                cardUpdate.setDescription(card.getDescription());
                cardUpdate.setDueDate(card.getDueDate());
                cardUpdate.setPosition(card.getPosition());
                cardUpdate.setCreatedAt(card.getCreatedAt());
                cardUpdate.setStatus(card.getStatus());
                cardUpdate.setLists(listRepository.findById(getIdList(String.valueOf(card.getLists()))).get());
                cardUpdate.setCreateBy(userRepository.findById(getIdUser(String.valueOf(card.getCreateBy()))).get());
                return cardRepository.save(cardUpdate);
            }).orElse(null);
        }
        return null;
    }

    @Override
    public Card detail(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        cardRepository.deleteById(id);
    }

    public Long getIdUser(String userName) {
        for (User user : userRepository.findAll()) {
            if (userName.equals(user.getUsername())) {
                return user.getId();
            }
        }
        return null;
    }

    public Long getIdList(String listName) {
        for (Lists lists : listRepository.findAll()) {
            if (listName.equals(lists.getName())) {
                return lists.getId();
            }
        }
        return null;
    }

    public void addMemberToCard(Long cardId, Long userId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        CardAssignment cardAssignment = new CardAssignment();
        cardAssignment.setCard(card);
        cardAssignment.setUser(user);
        cardAssignmentRepository.save(cardAssignment);

        emailService.sendEmail(user.getEmail(), "Assigned to Card", "You have been assigned to the card: " + card.getTitle());
    }

    // lấy địa chỉ email của người dùng dựa trên ID
    public String getUserEmailById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? user.getEmail() : null;
    }

    @EventListener
    public void handleCommentEvent(CommentEvent event) {
        Long cardId = event.getCardId();
        Long userId = event.getUserId();
        // Lấy danh sách các thành viên được gán cho card
        List<Long> assignedUserIds = cardAssignmentRepository.findAssignedUserIdsByCardId(cardId);
        // Gửi email thông báo cho mỗi thành viên được gán
        for (Long assignedUserId : assignedUserIds) {
            if (!assignedUserId.equals(userId)) { // Không gửi email cho người gửi bình luận/trả lời
                String userEmail = getUserEmailById(assignedUserId);
                String subject = "New Comment on Card";
                String message = "A new comment has been added to the card.";
                emailService.sendEmail(userEmail, subject, message);
            }
        }

    }

    @Override
    public void archiveCard(Long cardId, Long userId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new IllegalArgumentException("Card not found"));
        card.setStatus(true);
        cardRepository.save(card);

        historyService.record("cards", cardId, "ARCHIVE", "Card archived", userId);
    }

    @Override
    public void restoreCard(Long cardId, Long userId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new IllegalArgumentException("Card not found"));
        card.setStatus(false);
        cardRepository.save(card);

        historyService.record("cards", cardId, "ARCHIVE", "Card archived", userId);
    }
}