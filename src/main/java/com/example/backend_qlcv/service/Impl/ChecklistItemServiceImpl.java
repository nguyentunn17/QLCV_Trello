package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.Checklist;
import com.example.backend_qlcv.entity.ChecklistItem;
import com.example.backend_qlcv.repository.ChecklistItemRepository;
import com.example.backend_qlcv.repository.ChecklistRepository;
import com.example.backend_qlcv.service.ChecklistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChecklistItemServiceImpl implements ChecklistItemService {

    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    @Autowired
    private ChecklistRepository checklistRepository;

    @Override
    public List<ChecklistItem> getAll() {
        return checklistItemRepository.findAll();
    }

    @Override
    public ChecklistItem add(ChecklistItem checklistItem) {
        Optional<Checklist> checklistOptional = checklistRepository.findById(checklistItem.getChecklistId());
        if(!checklistOptional.isPresent()){
            throw new IllegalArgumentException("Checklist not found");
        }

        Checklist checklist = checklistOptional.get();
        checklistItem.setChecklist(checklist);

        ChecklistItem checklistItemSave = ChecklistItem.builder()
                .name(checklistItem.getName())
                .isCompleted(checklistItem.getIsCompleted())
                .position(checklistItem.getPosition())
                .checklistId(checklistItem.getChecklistId())
                .build();
        ChecklistItem saveChecklistItem = checklistItemRepository.save(checklistItemSave);
        return saveChecklistItem;
    }

    @Override
    public ChecklistItem update(ChecklistItem checklistItem, Long id) {

        Optional<Checklist> checklistOptional = checklistRepository.findById(checklistItem.getChecklistId());
        if(!checklistOptional.isPresent()){
            throw new IllegalArgumentException("Checklist not found");
        }
        Checklist checklist = checklistOptional.get();
        checklistItem.setChecklist(checklist);

        Optional<ChecklistItem> optionalChecklistItem = checklistItemRepository.findById(id);
        if (optionalChecklistItem.isPresent()){
            ChecklistItem updateChecklistItem =
            optionalChecklistItem.map(checklistItemUpdate -> {
                checklistItemUpdate.setName(checklistItem.getName());
                checklistItemUpdate.setIsCompleted(checklistItem.getIsCompleted());
                checklistItemUpdate.setPosition(checklistItem.getPosition());
                checklistItemUpdate.setChecklistId(checklistItem.getChecklistId());
            return checklistItemRepository.save(checklistItemUpdate);
            }).orElse(null);
            return updateChecklistItem;
        }

        return null;
    }

    @Override
    public ChecklistItem detail(Long id) {
        return checklistItemRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        checklistItemRepository.deleteById(id);
    }

    @Override
    public List<ChecklistItem> getChecklistItemByChecklistId(Long checklistId) {
        return checklistItemRepository.findByChecklistId(checklistId);
    }
    

    public Long getIdChecklist(String checklistName){
        for(Checklist checklist : checklistRepository.findAll()){
            if (checklistName.equals(checklist.getName())){
                return checklist.getId();
            }
        }
        return null;
    }
}
