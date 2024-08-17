package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.*;
import com.example.backend_qlcv.event.CommentEvent;
import com.example.backend_qlcv.repository.*;
import com.example.backend_qlcv.service.CardService;
import com.example.backend_qlcv.service.HistoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Service
public class CardServiceImpl implements CardService {

    CardRepository cardRepository;

    UserRepository userRepository;

    ListRepository listRepository;

    CardAssignmentRepository cardAssignmentRepository;

    ChecklistItemRepository checklistItemRepository;

    ChecklistRepository checklistRepository;

    CommentRepository commentRepository;

    AttachmentRepository attachmentRepository;

    CardLabelRepository cardLabelRepository;

    EmailService emailService;

    HistoryService historyService;

    HttpServletRequest request;

    /// Lấy thời gian hiện tại và tạo thành Timestamp
    Timestamp currentTime = new Timestamp(System.currentTimeMillis());

    @Override
    public List<Card> getAll() {
        return cardRepository.findAll();
    }

    @Override
    public Card add(Card card) {

        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Lists lists = listRepository.findById(card.getListsId())
                .orElseThrow(() -> new IllegalArgumentException("List not found"));

        Card cardSave = Card.builder()
                .title(card.getTitle())
                .description(card.getDescription())
                .dueDate(card.getDueDate())
                .position(card.getPosition())
                .completed(card.getCompleted())
                .createdAt(currentTime)
                .status(card.getStatus())
                .createBy(user)
                .lists(lists)
                .listsId(card.getListsId())
                .build();

        return cardRepository.save(cardSave);
    }

    @Override
    public Card update(Card card, Long id) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));


        Optional<Lists> listsOptional = listRepository.findById(card.getListsId());
        if(!listsOptional.isPresent()){
            System.out.println("Lists not found with ID: " + card.getListsId());
            throw new IllegalArgumentException("Lists not found");
        }

        Lists lists = listsOptional.get();
        card.setLists(lists);

        Optional<Card> optionalCard = cardRepository.findById(id);
        if (optionalCard.isPresent()) {
            Card updateCard = optionalCard.map(cardUpdate -> {
                cardUpdate.setTitle(card.getTitle());
                cardUpdate.setDescription(card.getDescription());
                cardUpdate.setStartDate(card.getStartDate());
                cardUpdate.setDueDate(card.getDueDate());
                cardUpdate.setCompleted(card.getCompleted());
                cardUpdate.setPosition(card.getPosition());
                cardUpdate.setCreatedAt(card.getCreatedAt());
                cardUpdate.setStatus(card.getStatus());
                cardUpdate.setListsId(card.getListsId());
                cardUpdate.setCreateBy(user);
                return cardRepository.save(cardUpdate);
            }).orElse(null);
            System.out.println("Update successful for card with ID: " + id);
            return updateCard;
        }
        System.out.println("Card not found with ID: " + id);
        return null;
    }

    @Override
    public Card detail(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteCard(Long cardId) {
        // Kiểm tra xem thẻ có tồn tại hay không
        Optional<Card> cardOptional = cardRepository.findById(cardId);
        if (cardOptional.isEmpty()) {
            throw new IllegalArgumentException("Card not found with id: " + cardId);
        }

        // Xóa các mục checklist liên quan đến thẻ
        checklistItemRepository.deleteByCardId(cardId);

        // Xóa các danh sách kiểm tra thuộc về thẻ
        checklistRepository.deleteByCardId(cardId);

        // Xóa các bình luận liên quan đến thẻ
        commentRepository.deleteByCardId(cardId);

        // Xóa các tệp đính kèm liên quan đến thẻ
        attachmentRepository.deleteByCardId(cardId);

        // Xóa các liên kết giữa thẻ và nhãn
        cardLabelRepository.deleteByCardId(cardId);

        // Xóa các phân công thẻ
        cardAssignmentRepository.deleteByCardId(cardId);

        // Cuối cùng, xóa thẻ chính
        cardRepository.deleteById(cardId);
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

    @Transactional
    public void addMemberToCard(Long cardId, List<Long> userIds, Long adderId) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found"));

        Long userIdAdder = (Long) request.getAttribute("userId");
        User userAdder = userRepository.findById(userIdAdder).orElseThrow(() -> new RuntimeException("User not found"));

        for (Long userId : userIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));


            CardAssignment cardAssignment = new CardAssignment();
            cardAssignment.setCard(card);
            cardAssignment.setUser(user);
            cardAssignmentRepository.save(cardAssignment);

            emailService.sendEmail(user.getEmail(), "Lời nhắc!!! ", "Bạn đã được thêm vào thẻ: " + card.getTitle()+ "\" bởi " + userAdder.getFullName() + ".");
        }
    }

    @Override
    @Transactional
    public void removeMemberFromCard(Long cardId, Long userId) {
        cardAssignmentRepository.deleteByCardIdAndUserId(cardId, userId);
    }

    @Override
    public List<User> getMembersInCard(Long cardId) {
        return cardAssignmentRepository.findUsersByCardId(cardId);
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
                String subject = "Một bình luận mới ";
                String message = "Một bình luận mới được thêm vào thẻ.";
                emailService.sendEmail(userEmail, subject, message);
            }
        }

    }


    // Bỏ lưu trữ card (Xét status = true và cập nhật history)
    @Transactional
    @Override
    public void archiveCard(Long cardId) {
        Long userId = (Long) request.getAttribute("userId");
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new IllegalArgumentException("Card not found"));
        card.setStatus(true);
        cardRepository.save(card);

        historyService.record("Thẻ", cardId, "Bỏ lưu trữ", "Thẻ đã được bỏ lưu trữ", userId);
    }

    // Lưu trữ card (Xét status = false và cập nhật history)
    @Transactional
    @Override
    public void restoreCard(Long cardId) {
        Long userId = (Long) request.getAttribute("userId");
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new IllegalArgumentException("Card not found"));
        card.setStatus(false);
        cardRepository.save(card);

        historyService.record("Thẻ", cardId, "Lưu trữ", "Thẻ đã được lưu trũ", userId);
    }

    // Hiện thị card theo listId
    @Override
    public List<Card> getCardsByListsId(Long listId) {
        return cardRepository.findByListsId(listId);
    }
}