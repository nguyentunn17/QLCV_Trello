package com.example.backend_qlcv.controller;


import com.example.backend_qlcv.entity.Board;
import com.example.backend_qlcv.model.request.AddBoardWithMembersRequest;
import com.example.backend_qlcv.model.request.AddMembersRequest;
import com.example.backend_qlcv.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/board/")
@RestController
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private HttpServletRequest request;

    // Nếu là admin thì hiện thị tất cả bảng còn user thì hiện thị những bạn được add
    @GetMapping("get-all")
    public ResponseEntity getAllBoards() {
        return new ResponseEntity(boardService.getAllBoards(), HttpStatus.OK);
    }


    // Detail
    @GetMapping("{id}")
    public ResponseEntity detail(@PathVariable("id") Long id) {
        return new ResponseEntity(boardService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    // Thêm bảng, tạo label, thêm thành viên vào bảng
    @PostMapping("add-all")
    public ResponseEntity addBoardWithMembers(@RequestBody AddBoardWithMembersRequest request ) {
        Board board = request.getBoard();
        List<Long> userIds = request.getUserIds();
        Board createdBoard = boardService.addBoardWithMembers(board, userIds);
        return new ResponseEntity<>(createdBoard, HttpStatus.OK);
    }


    // Cập nhật lại bảng
    @PutMapping("update/{boardId}")
    public ResponseEntity<Board> update(@PathVariable Long boardId, @RequestParam String newName) {
        Board updatedBoard = boardService.updateBoardName(boardId, newName);
        return new ResponseEntity<>(updatedBoard, HttpStatus.OK);
    }


    // Xoá bảng (chưa dùng)
    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        boardService.delete(Long.valueOf(id.toString()));
    }

    // Tìm kiếm bảng (chưa dùng)
    @GetMapping("search")
    public ResponseEntity search(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo, @RequestParam(name = "keyWord") String keyWord) {
        return new ResponseEntity(boardService.search(pageNo, keyWord), HttpStatus.OK);
    }

    // Lọc bảng theo trạng thái
    @GetMapping("loc")
    public ResponseEntity loc(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo, @RequestParam(name = "status")  Integer status) {
        return new ResponseEntity(boardService.loc(pageNo, status), HttpStatus.OK);
    }

    // Thêm thành viên vào bảng
    @PostMapping("{boardId}/members")
    public ResponseEntity<Void> addMembersToBoard(
            @PathVariable Long boardId,
            @RequestBody AddMembersRequest addMembersRequest) {
        boardService.addMembersToBoard(boardId, addMembersRequest.getUserIds(), addMembersRequest.getAdderId());
        return ResponseEntity.ok().build();
    }

    // ADMIN trong user_board đóng bảng và lưu trữ vào history
    @PostMapping("close/{boardId}")
    public ResponseEntity<Map<String, String>> closeBoard(@PathVariable Long boardId) {
        try {
            boardService.closeBoard(boardId);
            return buildResponse("Successfully close the board.", false);
        } catch (Exception e) {
            return buildResponse("Error close board: " + e.getMessage(), true);
        }
    }

    // ADMIN trong user_board mở bảng trong history
    @PostMapping("restore/{boardId}")
    public ResponseEntity<Map<String, String>> restoreBoard(@PathVariable Long boardId) {
        try {
            boardService.restoreBoard(boardId);
            return buildResponse("Successfully restore the board.", false);
        } catch (Exception e) {
            return buildResponse("Error restore board: " + e.getMessage(), true);
        }
    }

    // Thành viên đó tự ròi khỏi board
    @DeleteMapping("leave/{boardId}")
    public ResponseEntity<Map<String, String>> leaveBoard(@PathVariable Long boardId) {
        Long userId = (Long) request.getAttribute("userId");
        try {
            boardService.leaveBoard(boardId, userId);
            return buildResponse("You have successfully left the board.", false);
        } catch (Exception e) {
            return buildResponse("Error leaving board: " + e.getMessage(), true);
        }
    }

    // Xoá thành viên ra khỏi board
    @DeleteMapping("remove/{boardId}/{userId}")
    public ResponseEntity<Map<String, String>> removeUserFromBoard(@PathVariable Long boardId, @PathVariable Long userId) {
        try {
            boardService.leaveBoard(boardId, userId);
            return buildResponse("User has been successfully removed from the board.", false);
        } catch (Exception e) {
            return buildResponse("Error removing user from board: " + e.getMessage(), true);
        }
    }
    private ResponseEntity<Map<String, String>> buildResponse(String message, boolean isError) {
        Map<String, String> response = new HashMap<>();
        response.put(isError ? "error" : "message", message);
        return ResponseEntity.status(isError ? 500 : 200).body(response);
    }
}
