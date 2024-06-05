package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.Card;
import com.example.backend_qlcv.entity.CardAssignment;
import com.example.backend_qlcv.entity.User;
import com.example.backend_qlcv.repository.CardAssignmentRepository;
import com.example.backend_qlcv.repository.CardRepository;
import com.example.backend_qlcv.repository.UserRepository;
import com.example.backend_qlcv.service.CardAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardAssignmentServiceImpl implements CardAssignmentService {

    @Autowired
    private CardAssignmentRepository cardAssignmentRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CardAssignment> getAll() {
        return cardAssignmentRepository.findAll();
    }

    @Override
    public CardAssignment add(CardAssignment cardAssignment) {
        CardAssignment cardAssignmentSave = CardAssignment.builder()
                .user(userRepository.findById(getIdUser(String.valueOf(cardAssignment.getUser()))).get())
                .card(cardRepository.findById(getIdCard(String.valueOf(cardAssignment.getCard()))).get())
                .build();
        cardAssignmentRepository.save(cardAssignmentSave);
        return null;
    }

    @Override
    public CardAssignment update(CardAssignment cardAssignment, Long id) {
        Optional<CardAssignment> optionalCardAssignment = cardAssignmentRepository.findById(id);
        if(optionalCardAssignment.isPresent()){
            optionalCardAssignment.map(cardAssignmentUpdate ->{
                cardAssignmentUpdate.setUser(userRepository.findById(getIdUser(String.valueOf(cardAssignment.getUser()))).get());
                cardAssignmentUpdate.setCard(cardRepository.findById(getIdCard(String.valueOf(cardAssignment.getCard()))).get());
                return cardAssignmentRepository.save(cardAssignmentUpdate);
            } ).orElse(null);
        }
        return null;
    }

    @Override
    public CardAssignment detail(Long id) {
        return cardAssignmentRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        cardAssignmentRepository.deleteById(id);
    }
    public Long getIdUser(String userName){
        for (User user  : userRepository.findAll()){
            if (userName.equals(user.getUsername())){
                return user.getId();
            }
        }
        return null;
    }
    public Long getIdCard(String cardName){
        for (Card card : cardRepository.findAll()){
            if(cardName.equals(card.getTitle())){
                return  card.getId();

            }
        }
        return null;
    }
}
