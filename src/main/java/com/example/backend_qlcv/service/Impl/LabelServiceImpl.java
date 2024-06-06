package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.Label;
import com.example.backend_qlcv.repository.LabelRepository;
import com.example.backend_qlcv.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
    private LabelRepository labelRepository;

    @Override
    public List<Label> getAll() {
        return labelRepository.findAll();
    }

    @Override
    public Label add(Label label) {
        Label labelSave = Label.builder()
                .name(label.getName())
                .color(label.getColor())
                .build();
        labelRepository.save(labelSave);
        return null;
    }

    @Override
    public Label update(Label label, Long id) {
        Optional<Label> optionalLabel = labelRepository.findById(id);
        if(optionalLabel.isPresent()){
            optionalLabel.map(labelUpdate ->{
                labelUpdate.setName(label.getName());
                labelUpdate.setColor(label.getColor());
                return labelRepository.save(labelUpdate);
            } ).orElse(null);
        }
        return null;
    }

    @Override
    public Label detail(Long id) {
        return labelRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        labelRepository.deleteById(id);
    }
}
