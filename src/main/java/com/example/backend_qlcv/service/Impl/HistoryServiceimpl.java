package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.History;
import com.example.backend_qlcv.entity.User;
import com.example.backend_qlcv.repository.HistoryRepository;
import com.example.backend_qlcv.repository.UserRepository;
import com.example.backend_qlcv.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryServiceimpl implements HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    // Lấy thời gian hiện tại và tạo thành Timestamp
    private final Timestamp currentTime = new Timestamp(System.currentTimeMillis());

    @Override
    public List<History> getAll() {
        return historyRepository.findAll();
    }

    @Override
    public History add(History history) {
       History historySave = History.builder()
               .tableName(history.getTableName())
               .recordId(history.getRecordId())
               .action(history.getAction())
               .changeDescription(history.getChangeDescription())
               .changedAt(currentTime)
               .user(userRepository.findById(getIdUser(String.valueOf(history.getUser()))).get())
               .build();
       historyRepository.save(historySave);
       return null;
    }

    @Override
    public History update(History history, Long id) {
        Optional<History> optionalHistory = historyRepository.findById(id);
        if(optionalHistory.isPresent()){
            optionalHistory.map(historyUpdate -> {
              historyUpdate.setTableName(history.getTableName());
              historyUpdate.setRecordId(history.getRecordId());
              historyUpdate.setAction(historyUpdate.getAction());
              historyUpdate.setChangeDescription(history.getChangeDescription());
              historyUpdate.setChangedAt(currentTime);
              historyUpdate.setUser(userRepository.findById(getIdUser(String.valueOf(history.getUser()))).get());
              return historyRepository.save(historyUpdate);
            }).orElse(null);
        }
        return null;
    }

    @Override
    public History detail(Long id) {
        return historyRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        historyRepository.deleteById(id);
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
