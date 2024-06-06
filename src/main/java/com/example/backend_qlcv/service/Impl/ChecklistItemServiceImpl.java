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
        ChecklistItem checklistItemSave = ChecklistItem.builder()
                .name(checklistItem.getName())
                .isComleted(checklistItem.getIsComleted())
                .position(checklistItem.getPosition())
                .checklist(checklistRepository.findById(getIdChecklist(String.valueOf(checklistItem.getChecklist()))).get())
                .build();
        checklistItemRepository.save(checklistItemSave);
        return null;
    }

    @Override
    public ChecklistItem update(ChecklistItem checklistItem, Long id) {
        Optional<ChecklistItem> optionalChecklistItem = checklistItemRepository.findById(id);
        if (optionalChecklistItem.isPresent()){
            optionalChecklistItem.map(checklistItemUpdate -> {
                checklistItemUpdate.setName(checklistItem.getName());
                checklistItemUpdate.setIsComleted(checklistItem.getIsComleted());
                checklistItemUpdate.setPosition(checklistItem.getPosition());
                checklistItemUpdate.setChecklist(checklistRepository.findById(getIdChecklist(String.valueOf(checklistItem.getChecklist()))).get());
            return checklistItemRepository.save(checklistItemUpdate);
            }).orElse(null);
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
    public Long getIdChecklist(String checklistName){
        for(Checklist checklist : checklistRepository.findAll()){
            if (checklistName.equals(checklist.getName())){
                return checklist.getId();
            }
        }
        return null;
    }
}
