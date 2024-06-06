package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.Card;
import com.example.backend_qlcv.entity.CardLabel;
import com.example.backend_qlcv.entity.Label;
import com.example.backend_qlcv.repository.CardLabelRepository;
import com.example.backend_qlcv.repository.CardRepository;
import com.example.backend_qlcv.repository.LabelRepository;
import com.example.backend_qlcv.service.CardLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardLabelServiceimpl implements CardLabelService
{
    @Autowired
    private CardLabelRepository cardLabelRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Override
    public List<CardLabel> getAll() {
        return cardLabelRepository.findAll();
    }

    @Override
    public CardLabel add(CardLabel cardLabel) {
        CardLabel cardLabelSave = CardLabel.builder()
                .card(cardRepository.findById(getIdCard(String.valueOf(cardLabel.getCard()))).get())
                .label(labelRepository.findById(getIdLabel(String.valueOf(cardLabel.getLabel()))).get())
                .build();
        cardLabelRepository.save(cardLabelSave);
        return null;
    }

    @Override
    public CardLabel update(CardLabel cardLabel, Long id) {
        Optional<CardLabel> optionalCardLabel = cardLabelRepository.findById(id);
        if(optionalCardLabel.isPresent()){
            optionalCardLabel.map(cardLabelUpdate -> {
                cardLabelUpdate.setCard(cardRepository.findById(getIdCard(String.valueOf(cardLabel.getCard()))).get());
                cardLabelUpdate.setLabel(labelRepository.findById(getIdLabel(String.valueOf(cardLabel.getLabel()))).get());
                return cardLabelRepository.save(cardLabelUpdate);
            }).orElse(null);
        }
        return null;
    }

    @Override
    public CardLabel detail(Long id) {
        return cardLabelRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        cardLabelRepository.deleteById(id);
    }
    public Long getIdCard(String cardName){
        for (Card card : cardRepository.findAll()){
            if(cardName.equals(card.getTitle())){
                return  card.getId();

            }
        }
        return null;
    }
    public Long getIdLabel(String labelName){
        for (Label label : labelRepository.findAll()){
            if(labelName.equals(label.getName())){
                return  label.getId();

            }
        }
        return null;
    }
}
