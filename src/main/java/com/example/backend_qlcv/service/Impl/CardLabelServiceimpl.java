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
import org.springframework.transaction.annotation.Transactional;

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
    public List<Label> getLabelsByCard(Long cardId) {
        return cardLabelRepository.findLabelsByCard(cardId);
    }


    @Override
    public CardLabel add(CardLabel cardLabel) {
        Card card = cardRepository.findById(cardLabel.getCard().getId()).orElseThrow(() -> new RuntimeException("Card not found"));
        Label label = labelRepository.findById(cardLabel.getLabel().getId()).orElseThrow(() -> new RuntimeException("Label not found"));
        CardLabel cardLabelSave = CardLabel.builder()
                .card(card)
                .label(label)
                .build();
        return cardLabelRepository.save(cardLabelSave);
    }
    @Override
    public void addColorsToCard(Long cardId, List<Long> labelIds) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        for (Long labelId : labelIds) {
            Label label = labelRepository.findById(labelId)
                    .orElseThrow(() -> new RuntimeException("Label not found"));

            CardLabel cardLabelSave = CardLabel.builder()
                    .card(card)
                    .label(label)
                    .build();
           cardLabelRepository.save(cardLabelSave);
        }
    }

    @Override
    @Transactional
    public void removeLabelByCard(Long cardId, Long labelId) {
        cardLabelRepository.deleteByCardIdAndLabelId(cardId, labelId);
    }

    @Override
    @Transactional
    public void removeLabelByLabelId(Long labelId){
        cardLabelRepository.deleteByLabelId(labelId);
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
