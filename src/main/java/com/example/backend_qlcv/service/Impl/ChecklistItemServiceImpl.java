package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.Checklist;
import com.example.backend_qlcv.entity.ChecklistItem;
import com.example.backend_qlcv.repository.ChecklistItemRepository;
import com.example.backend_qlcv.repository.ChecklistRepository;
import com.example.backend_qlcv.service.ChecklistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

        return null;
    }

    @Override
    public ChecklistItem update(ChecklistItem checklistItem, Long id) {
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
