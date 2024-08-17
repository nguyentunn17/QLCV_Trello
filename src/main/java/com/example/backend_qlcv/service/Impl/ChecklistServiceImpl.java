package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.*;
import com.example.backend_qlcv.repository.CardRepository;
import com.example.backend_qlcv.repository.ChecklistItemRepository;
import com.example.backend_qlcv.repository.ChecklistRepository;
import com.example.backend_qlcv.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ChecklistServiceImpl implements ChecklistService {

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    @Override
    public List<Checklist> getAll() {
        return checklistRepository.findAll();
    }

    @Override
    public Checklist add(Checklist checklist) {
        // Kiểm tra xem card có tồn tại hay không
        Optional<Card>  cardOptional = cardRepository.findById(checklist.getCardId());
        if (!cardOptional.isPresent()) {
            throw new IllegalArgumentException("Card not found");
        }
        // Thiết lập card cho checklist
        Card card = cardOptional.get();
        checklist.setCard(card);

        // Thiết lập các trường khác của checklist
        Checklist checklistSave = Checklist.builder()
                .name(checklist.getName())
                .cardId(checklist.getCardId())
                .build();

        // Lưu checklist vào cơ sở dữ liệu
        Checklist savedChecklist = checklistRepository.save(checklistSave);
        return savedChecklist;
    }



    @Override
    public Checklist update(Checklist checklist, Long id) {
        Optional<Card>  cardOptional = cardRepository.findById(checklist.getCardId());
        if (!cardOptional.isPresent()) {
            throw new IllegalArgumentException("Card not found");
        }
        // Thiết lập card cho checklist
        Card card = cardOptional.get();
        checklist.setCard(card);

        Optional<Checklist> optionalChecklist = checklistRepository.findById(id);
        if(optionalChecklist.isPresent()){
            Checklist updateChecklist = optionalChecklist.map(checklistUpdate ->{
                checklistUpdate.setName(checklist.getName());
                checklistUpdate.setCardId(checklist.getCardId());
                return checklistRepository.save(checklistUpdate);
            }).orElse(null);
            return updateChecklist;
        }
        return null;
    }

    @Override
    public Checklist detail(Long id) {
        return checklistRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void delete(Long checklistId) {
        // Kiểm tra xem checklist có tồn tại hay không
        if (checklistId == null || !checklistRepository.existsById(checklistId)) {
            throw new IllegalArgumentException("Checklist không tồn tại.");
        }

        // Kiểm tra xem có các checklist item nào không trước khi xóa
        List<ChecklistItem> items = checklistItemRepository.findByChecklistId(checklistId);
        if (items.isEmpty()) {
            System.out.println("Không có checklist item nào liên quan đến checklist này.");
        } else {
            // Xóa tất cả checklist items liên quan đến checklist này
            checklistItemRepository.deleteByChecklistId(checklistId);
        }

        // Xóa checklist sau khi đã xóa các items
        checklistRepository.deleteById(checklistId);
    }


    @Override
    public List<Checklist> getChecklistByCardId(Long cardId) {
        return checklistRepository.findByCardId(cardId);
    }

    public Long getIdCard(String cardName){
        for (Card card : cardRepository.findAll()){
            if(cardName.equals(card.getTitle())){
                return  card.getId();
            }
        }
        return null;
    }
}
