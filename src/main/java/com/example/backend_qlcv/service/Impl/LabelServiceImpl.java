package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.Board;
import com.example.backend_qlcv.entity.Label;
import com.example.backend_qlcv.repository.BoardRepository;
import com.example.backend_qlcv.repository.LabelRepository;
import com.example.backend_qlcv.service.LabelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Service
public class LabelServiceImpl implements LabelService {
    LabelRepository labelRepository;

    BoardRepository boardRepository;

    @Override
    public List<Label> getAll() {
        return labelRepository.findAll();
    }

    @Override
    public Label add(Label label) {
        Board board = boardRepository.findById(label.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Label labelSave = Label.builder()
                .name(label.getName())
                .color(label.getColor())
                .board(board)
                .boardId(label.getBoardId())
                .build();
        return labelRepository.save(labelSave);
    }

    @Override
    public Label update(Label label, Long id) {
        Optional<Board> boardOptional = boardRepository.findById(label.getBoardId());
        if(!boardOptional.isPresent()){
            System.out.println("Lists not found with ID: " + label.getBoardId());
            throw new IllegalArgumentException("Board not found");
        }
        Board board = boardOptional.get();
        label.setBoard(board);

        Optional<Label> optionalLabel = labelRepository.findById(id);
        if(optionalLabel.isPresent()){
            Label updateLabel = optionalLabel.map(labelUpdate ->{
                labelUpdate.setName(label.getName());
                labelUpdate.setColor(label.getColor());
                return labelRepository.save(labelUpdate);
            } ).orElse(null);
            System.out.println("Update successful for board with ID: " + id);
            return updateLabel;
        }
        System.out.println("Card not found with ID: " + id);
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

    @Override
    public List<Label> getLabelByBoardId(Long boardId) {
        return labelRepository.findByBoardId(boardId);
    }
}
