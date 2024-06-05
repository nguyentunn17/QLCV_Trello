package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.Board;
import com.example.backend_qlcv.entity.Lists;
import com.example.backend_qlcv.repository.BoardRepository;
import com.example.backend_qlcv.repository.ListRepository;
import com.example.backend_qlcv.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ListServiceImpl implements ListService {

    @Autowired
    private ListRepository listRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Override
    public java.util.List<Lists> getAll() {
        return listRepository.findAll();
    }

    @Override
    public Lists add(Lists lists) {
        Lists listsSave = Lists.builder()
                .name(lists.getName())
                .position(lists.getPosition())
                .board(boardRepository.findById(getIdBoard(String.valueOf(lists.getBoard()))).get())
                .build();
        listRepository.save(listsSave);
        return null;
    }

    @Override
    public Lists update(Lists lists, Long id) {
        Optional<Lists> optionalList = listRepository.findById(id);
        if (optionalList.isPresent()){
            optionalList.map(listUpdate -> {
                listUpdate.setName(lists.getName());
                listUpdate.setPosition(lists.getPosition());
                listUpdate.setBoard(boardRepository.findById(getIdBoard(String.valueOf(lists.getBoard()))).get());
            return listRepository.save(listUpdate);
            }).orElse(null);
        }
        return null;
    }

    @Override
    public Lists detail(Long id) {
        return listRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        listRepository.deleteById(id);
    }
    public Long getIdBoard(String boardName){
        for (Board board  : boardRepository.findAll()){
            if (boardName.equals(board.getName())){
                return board.getId();
            }
        }
        return null;
    }
}
