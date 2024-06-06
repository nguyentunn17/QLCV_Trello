package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.Board;
import com.example.backend_qlcv.entity.Card;
import com.example.backend_qlcv.entity.Comment;
import com.example.backend_qlcv.entity.User;
import com.example.backend_qlcv.repository.CardRepository;
import com.example.backend_qlcv.repository.CommentRepository;
import com.example.backend_qlcv.repository.UserRepository;
import com.example.backend_qlcv.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    // Lấy thời gian hiện tại và tạo thành Timestamp
    private final Timestamp currentTime = new Timestamp(System.currentTimeMillis());

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment add(Comment comment) {
        Comment commentSave = Comment.builder()
                .content(comment.getContent())
                .createdAt(currentTime)
                .user(userRepository.findById(getIdUser(String.valueOf(comment.getUser()))).get())
                .card(cardRepository.findById(getIdCard(String.valueOf(comment.getCard()))).get())
                .build();
         commentRepository.save(comment);
        return null;
    }

    @Override
    public Comment update(Comment comment, Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if(optionalComment.isPresent()){
            optionalComment.map(commentUpdate -> {
                commentUpdate.setContent(comment.getContent());
                commentUpdate.setCreatedAt(currentTime);
                commentUpdate.setUser(userRepository.findById(getIdUser(String.valueOf(comment.getUser()))).get());
                commentUpdate.setCard(cardRepository.findById(getIdCard(String.valueOf(comment.getCard()))).get());
                return commentRepository.save(commentUpdate);
            }).orElse(null);
        }
        return null;
    }

    @Override
    public Comment detail(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
    public Long getIdCard(String cardName){
        for (Card card : cardRepository.findAll()){
            if(cardName.equals(card.getTitle())){
                return  card.getId();

            }
        }
        return null;
    }
    public Long getIdUser(String userName){
        for (User user  : userRepository.findAll()){
            if (userName.equals(user.getUsername())){
                return user.getId();
            }
        }
        return null;
    }
}
