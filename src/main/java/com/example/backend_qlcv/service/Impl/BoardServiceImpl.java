package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.constant.ERoleUserBoard;
import com.example.backend_qlcv.entity.*;
import com.example.backend_qlcv.repository.*;
import com.example.backend_qlcv.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserBoardRepository userBoardRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private CardAssignmentRepository cardAssignmentRepository;

    @Autowired
    private HistoryRepository historyRepository;


    @Autowired
    private HttpServletRequest request;

    /// Lấy thời gian hiện tại và tạo thành Timestamp
    Timestamp currentTime = new Timestamp(System.currentTimeMillis());

    @Override
    public List<Board> getAll() {
        return boardRepository.findAll();
    }

    @Override
    public List<Board> getAllBoards() {
        String roles = (String) request.getAttribute("roles");
        String username = (String) request.getAttribute("username");
        if (roles.contains("ROLE_ADMIN")) {
            return boardRepository.getAll();
        } else {
            return boardRepository.findBoardsByUsername(username);
        }
    }


    @Transactional
    public Board addBoardWithMembers(Board board, List<Long> userIds) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Board boardSave = Board.builder()
                .name(board.getName())
                .description(board.getDescription())
                .createdAt(currentTime) // currentTime đã được khai báo trước đó
                .status(1) // Giả sử 1 là status cho board mới được tạo
                .createdBy(user)
                .build();

        Board createdBoard = boardRepository.save(boardSave);

        // Tạo các label mặc định
        List<Label> defaultLabels = List.of(
                Label.builder().name("").color("#4BCE97").board(createdBoard).build(),
                Label.builder().name("").color("#F5CD47").board(createdBoard).build(),
                Label.builder().name("").color("#FEA362").board(createdBoard).build(),
                Label.builder().name("").color("#F87168").board(createdBoard).build(),
                Label.builder().name("").color("#9F8FEF").board(createdBoard).build(),
                Label.builder().name("").color("#579DFF").board(createdBoard).build()
        );

        defaultLabels.forEach(label -> {
            label.setBoardId(createdBoard.getId());
            label.setBoard(createdBoard);
        });

        labelRepository.saveAll(defaultLabels);

        // Thêm user tạo board như ADMIN
        UserBoard adminUserBoard = UserBoard.builder()
                .role("ADMIN")
                .assignedAt(currentTime)
                .user(user)
                .status(1)
                .board(createdBoard)
                .build();

        userBoardRepository.save(adminUserBoard);

        // Thêm các thành viên vào board
        for (Long userIdToAdd : userIds) {
            User userToAdd = userRepository.findById(userIdToAdd)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UserBoard userBoard = UserBoard.builder()
                    .board(createdBoard)
                    .user(userToAdd)
                    .role("MEMBER")
                    .status(1)
                    .assignedAt(currentTime)
                    .build();

            userBoardRepository.save(userBoard);

            // Gửi email thông báo từ địa chỉ email mặc định
            String subject = "Bạn đã được thêm vào một board mới";
            String text = "Chào " + userToAdd.getFullName() + ",\n\nBạn đã được thêm vào board \"" + createdBoard.getName() + "\" bởi " + user.getFullName() + ".";
            emailService.sendEmail(userToAdd.getEmail(), subject, text);
        }

        return createdBoard;
    }


    @Override
    public Board update(Board board, Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (optionalBoard.isPresent()) {
            optionalBoard.map(boardUpdate -> {
                boardUpdate.setName(board.getName());
                boardUpdate.setDescription(board.getDescription());
                boardUpdate.setCreatedBy(userRepository.findById(getIdUser(String.valueOf(board.getCreatedBy()))).get());
                return boardRepository.save(boardUpdate);
            }).orElse(null);
        }
        return null;
    }

    @Override
    public Board detail(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    @Override
    public Page<Board> search(Integer pageNo, String keyWord) {
        Pageable pageable = PageRequest.of(pageNo, 10);
        return boardRepository.searchByKeyword(pageable, keyWord);
    }

    public Long getIdUser(String userName) {
        for (User user : userRepository.findAll()) {
            if (userName.equals(user.getUsername())) {
                return user.getId();
            }
        }
        return null;
    }

//    @Transactional
//    public void addMembersToBoard(Long boardId, List<Long> userIds, Long adderId) {
//        Board board = boardRepository.findById(boardId)
//                .orElseThrow(() -> new RuntimeException("Board not found"));
//
//        Long userIdAdder = (Long) request.getAttribute("userId");
//        User userAdder = userRepository.findById(userIdAdder).orElseThrow(() -> new RuntimeException("User not found"));
//
//        for (Long userId : userIds) {
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            // Thêm thành viên vào board thông qua UserBoard
//            UserBoard userBoard = new UserBoard();
//            userBoard.setBoard(board);
//            userBoard.setUser(user);
//            userBoard.setRole("MEMBER");
//            userBoard.setAssignedAt(currentTime);
//
//            userBoardRepository.save(userBoard);
//
//            // Gửi email thông báo từ địa chỉ email mặc định
//            String subject = "Bạn đã được thêm vào một board mới";
//            String text = "Chào " + user.getFullName() + ",\n\nBạn đã được thêm vào board \"" + board.getName() + "\" bởi " + userAdder.getFullName() + ".";
//            emailService.sendEmail(user.getEmail(), subject, text);
//        }
//    }

    @Transactional
    public void addMembersToBoard(Long boardId, List<Long> userIds, Long adderId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        Long userIdAdder = (Long) request.getAttribute("userId");
        User userAdder = userRepository.findById(userIdAdder).orElseThrow(() -> new RuntimeException("User not found"));

        for (Long userId : userIds) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Kiểm tra xem user đã từng là thành viên của board chưa
            Optional<UserBoard> existingUserBoard = userBoardRepository.findByUserIdAndBoardId(userId, boardId);

            if (existingUserBoard.isPresent()) {
                // Nếu user đã từng là thành viên, cập nhật status thành 1
                UserBoard userBoard = existingUserBoard.get();
                userBoard.setStatus(1);
                userBoard.setAssignedAt(currentTime); // Cập nhật thời gian được thêm vào lại
                userBoardRepository.save(userBoard);
            } else {
                // Nếu user chưa từng là thành viên, thêm mới
                UserBoard userBoard = new UserBoard();
                userBoard.setBoard(board);
                userBoard.setUser(user);
                userBoard.setRole("MEMBER");
                userBoard.setStatus(1); // Mặc định là 1 khi mới thêm vào
                userBoard.setAssignedAt(currentTime);
                userBoardRepository.save(userBoard);
            }

            // Gửi email thông báo từ địa chỉ email mặc định
            String subject = "Bạn đã được thêm vào một board mới";
            String text = "Chào " + user.getFullName() + ",\n\nBạn đã được thêm vào board \"" + board.getName() + "\" bởi " + userAdder.getFullName() + ".";
            emailService.sendEmail(user.getEmail(), subject, text);
        }
    }



    @Override
    public Page<Board> loc(Integer pageNo, Integer status) {
        Pageable pageable = PageRequest.of(pageNo, 10);
        return boardRepository.loc(pageable, status);
    }

    @Transactional
    @Override
    public Board updateBoardName(Long boardId, String newName) {
        Long userId = (Long) request.getAttribute("userId");
        // Kiểm tra vai trò của người dùng
        boolean isAdmin = userBoardRepository.findByBoardIdAndUserIdAndRole(boardId, userId, ERoleUserBoard.ADMIN.getRole()).isPresent();

        if (isAdmin) {
            // Cập nhật tên board nếu người dùng là ADMIN
            Board board = boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Board not found"));
            board.setName(newName);
            return boardRepository.save(board);
        } else {
            throw new RuntimeException("You do not have permission to update the board name.");
        }
    }

    // ADMIN trong user_board đóng bảng và lưu trữ vào history
    @Override
    public void closeBoard(Long boardId) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            board.setStatus(0); // 0 for Closed
            boardRepository.save(board);

            // Lưu hành động vào bảng history
            History history = new History();
            history.setUser(user);
            history.setTableName("Bảng");
            history.setRecordId(boardId);
            history.setAction("Đóng");
            history.setChangeDescription("Bảng đã được đóng bởi ADMIN");
            history.setChangedAt(currentTime);
            historyRepository.save(history);
        }
    }

    // ADMIN trong user_board mở bảng trong history
    @Override
    public void restoreBoard(Long boardId) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            board.setStatus(1); // 1 for Active
            boardRepository.save(board);

            // Lưu hành động vào bảng history
            History history = new History();
            history.setUser(user);
            history.setTableName("Bảng");
            history.setRecordId(boardId);
            history.setAction("Mở");
            history.setChangeDescription("Bảng đã được mở bởi ADMIN");
            history.setChangedAt(currentTime);
            historyRepository.save(history);
        }
    }

    // Xoá thành viên ra khỏi board hoặc thành viên đó tự ròi khỏi bảng
    @Transactional
    @Override
    public void leaveBoard(Long boardId, Long userId) {
        // Lấy danh sách người dùng từ bảng theo boardId
        List<User> usersInBoard = userRepository.getUserByBoard(boardId);

        // Kiểm tra xem userId có nằm trong danh sách không
        boolean userExistsInBoard = usersInBoard.stream().anyMatch(user -> user.getId().equals(userId));

        if (userExistsInBoard) {
            // Cập nhật trạng thái của người dùng trong bảng user_boards
            userBoardRepository.updateUserBoardStatus(userId, boardId, 0);

            // Xóa thông tin người dùng từ cardAssignment
            cardAssignmentRepository.deleteByBoardIdAndUserId(boardId, userId);

            // Có thể thêm thông báo thành công hoặc ghi log nếu cần
            // Ví dụ:
            // log.info("User {} removed from board {}", userId, boardId);
        } else {
            throw new RuntimeException("User is not a member of the board.");
        }
    }


}
