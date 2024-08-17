package com.example.backend_qlcv.model.request;

import com.example.backend_qlcv.entity.Board;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddBoardWithMembersRequest {

    Board board;

    List<Long> userIds;
}
