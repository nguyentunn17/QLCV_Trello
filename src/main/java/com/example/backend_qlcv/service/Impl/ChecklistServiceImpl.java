package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.Card;
import com.example.backend_qlcv.entity.Checklist;
import com.example.backend_qlcv.repository.CardRepository;
import com.example.backend_qlcv.repository.ChecklistRepository;
import com.example.backend_qlcv.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChecklistServiceImpl implements ChecklistService {

    @Autowired
    private ChecklistRepository checklistRepository;

    @Autowired
    private CardRepository cardRepository;

    @Override
    public List<Checklist> getAll() {
        return checklistRepository.findAll();
    }

    @Override
    public Checklist add(Checklist checklist) {
        Checklist checklistSave = Checklist.builder()
                .name(checklist.getName())
                .card(cardRepository.findById(getIdCard(String.valueOf(checklist.getCard()))).get())
                .build();
        return checklistRepository.save(checklistSave);
    }

    @Override
    public Checklist update(Checklist checklist, Long id) {
        Optional<Checklist> optionalChecklist = checklistRepository.findById(id);
        if(optionalChecklist.isPresent()){
            optionalChecklist.map(checklistUpdate ->{
                checklistUpdate.setName(checklist.getName());
                checklistUpdate.setCard(cardRepository.findById(getIdCard(String.valueOf(checklist.getCard()))).get());
                return checklistRepository.save(checklistUpdate);
            }).orElse(null);
        }
        return null;
    }

    @Override
    public Checklist detail(Long id) {
        return checklistRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        checklistRepository.deleteById(id);
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
