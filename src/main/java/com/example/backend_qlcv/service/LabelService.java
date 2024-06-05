package com.example.backend_qlcv.service;

import com.example.backend_qlcv.entity.Label;

import java.util.List;

public interface LabelService {
    List<Label> getAll();

    Label add(Label label);

    Label update(Label label, Long id);

    Label detail(Long id);

    void delete(Long id);
}
