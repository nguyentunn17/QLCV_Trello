package com.example.backend_qlcv.service.Impl;

import com.example.backend_qlcv.entity.Attachment;
import com.example.backend_qlcv.entity.Card;
import com.example.backend_qlcv.repository.AttachmentRepository;
import com.example.backend_qlcv.repository.CardRepository;
import com.example.backend_qlcv.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private CardRepository cardRepository;

    @Override
    public List<Attachment> getAll() {
        return attachmentRepository.findAll();
    }

    @Override
    public Attachment add(Attachment attachment) {
        Attachment attachmentSave = Attachment.builder()
                .filePath(attachment.getFilePath())
                .uploadAt(attachment.getUploadAt())
                .card(cardRepository.findById(getIdCard(String.valueOf(attachment.getCard()))).get())
                .build();
        attachmentRepository.save(attachmentSave);
        return null;
    }

    @Override
    public Attachment update(Attachment attachment, Long id) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);
        if(optionalAttachment.isPresent()){
            optionalAttachment.map(attachmentUpdate -> {
                attachmentUpdate.setFilePath(attachment.getFilePath());
                attachmentUpdate.setUploadAt(attachment.getUploadAt());
                attachmentUpdate.setCard(cardRepository.findById(getIdCard(String.valueOf(attachment.getCard()))).get());
            return attachmentRepository.save(attachmentUpdate);
            }).orElse(null);
        }
        return null;
    }

    @Override
    public Attachment detail(Long id) {
        return attachmentRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        attachmentRepository.deleteById(id);
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
