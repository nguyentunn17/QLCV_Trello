package com.example.backend_qlcv.event;

import com.example.backend_qlcv.entity.Comment;
import org.springframework.context.ApplicationEvent;

public class CommentEvent extends ApplicationEvent {
    private final Comment comment;

    public CommentEvent(Object source, Comment comment) {
        super(source);
        this.comment = comment;
    }

    public Long getCardId() {
        return comment.getCard().getId();
    }

    public Long getUserId() {
        return comment.getUser().getId();
    }
}
