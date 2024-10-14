package com.springboot.supportdocuments.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.supportdocuments.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/document")
public class DocumentController {

    final private DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

//    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity postImage(@Valid @RequestPart("imageDto") ImageDto imageDto,
//                                    @RequestPart("multipartFile") MultipartFile multipartFile){
//        imageService.store(multipartFile);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    @PostMapping
    public ResponseEntity<?> uploadImage(
            @RequestParam("multipartFile") MultipartFile multipartFile) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ImageDto dto = objectMapper.readValue(imageDto, ImageDto.class);

        String uri = documentService.store(multipartFile);

        return new ResponseEntity<>(
                uri,
                HttpStatus.OK);
    }
}
