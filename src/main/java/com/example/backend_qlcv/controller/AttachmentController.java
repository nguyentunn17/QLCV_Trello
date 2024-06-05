package com.example.backend_qlcv.controller;

import com.example.backend_qlcv.entity.Attachment;
import com.example.backend_qlcv.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attachment/")
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;

    @GetMapping("get-all")
    public List<Attachment> getAll() {return  attachmentService.getAll();}

    @GetMapping("detail/{id}")
    public ResponseEntity detail(@PathVariable("id") String id){
        return new ResponseEntity(attachmentService.detail(Long.valueOf(id.toString())), HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Attachment attachment){
        return new ResponseEntity<>(attachmentService.add(attachment), HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity update(@RequestBody Attachment attachment, @PathVariable("id") String id){
        return new ResponseEntity(attachmentService.update(attachment, Long.valueOf(id.toString())), HttpStatus.OK);
    }
    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") String id){
        attachmentService.delete(Long.valueOf(id.toString()));
    }
}
