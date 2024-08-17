package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.Board;
import com.example.backend_qlcv.entity.Lists;
import com.example.backend_qlcv.repository.BoardRepository;
import com.example.backend_qlcv.repository.HistoryRepository;
import com.example.backend_qlcv.repository.ListRepository;
import com.example.backend_qlcv.service.HistoryService;
import com.example.backend_qlcv.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ListServiceImpl implements ListService {

    @Autowired
    private ListRepository listRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private HistoryService historyService;

    @Override
    public java.util.List<Lists> getAll() {
        return listRepository.findAll();
    }

    @Override
    public Lists add(Lists lists) {
        // Kiểm tra xem board có tồn tại hay không
        Optional<Board> boardOptional = boardRepository.findById(lists.getBoardId());
        if (!boardOptional.isPresent()) {
            throw new IllegalArgumentException("Board not found");
        }

        // Thiết lập board cho list
        Board board = boardOptional.get();
        lists.setBoard(board);

        // Thiết lập các trường khác của list
        Lists listsSave = Lists.builder()
                .name(lists.getName())
                .position(lists.getPosition())
                .status(lists.getStatus())
                .boardId(lists.getBoardId())
                .build();

        // Lưu list vào cơ sở dữ liệu
        Lists savedList = listRepository.save(listsSave);
        return savedList;
    }

    @Override
    public Lists update(Lists lists, Long id) {
        Optional<Board> boardOptional = boardRepository.findById(lists.getBoardId());
        if (!boardOptional.isPresent()) {
            throw new IllegalArgumentException("Board not found");
        }

        // Thiết lập board cho list
        Board board = boardOptional.get();
        lists.setBoard(board);

        Optional<Lists> optionalList = listRepository.findById(id);
        if (optionalList.isPresent()){
            Lists updatedList = optionalList.map(listUpdate -> {
                listUpdate.setName(lists.getName());
                listUpdate.setBoardId(lists.getBoardId());
                return listRepository.save(listUpdate);
            }).orElse(null);
            return updatedList;
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

    @Override
    public void archiveList(Long listId, Long userId) {
        Lists lists = listRepository.findById(listId).orElseThrow(() -> new IllegalArgumentException("List not found"));
        lists.setStatus(true);
        listRepository.save(lists);

        historyService.record("lists", listId, "RESTORE", "List restored from archive", userId);
    }

    @Override
    public void restoreList(Long listId, Long userId) {
        Lists lists = listRepository.findById(listId).orElseThrow(() -> new IllegalArgumentException("List not found"));
        lists.setStatus(false);
        listRepository.save(lists);

        historyService.record("lists", listId, "RESTORE", "List restored from archive", userId);
    }

    @Override
    public List<Lists> getListsByBoardId(Long boardId) {
        return listRepository.findByBoardId(boardId);
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
