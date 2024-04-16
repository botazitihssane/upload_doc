package fr.norsys.upload_doc.controller;


import fr.norsys.upload_doc.entity.Document;
import fr.norsys.upload_doc.service.impl.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import fr.norsys.upload_doc.dto.DocumentDetailsResponse;
import fr.norsys.upload_doc.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("api/document")
@AllArgsConstructor
public class DocumentController {

    @Autowired
    private DocumentServiceImpl documentServiceImpl;
    @PostMapping("/save")
    public ResponseEntity<?> saveDocument(@ModelAttribute Document document, @RequestParam("file") MultipartFile multipartFile) {
        return documentServiceImpl.save(document, multipartFile);
    }




    private final DocumentService documentService;

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDetailsResponse> getDocumentByUUID(@PathVariable UUID id) {
        try {
            DocumentDetailsResponse response = documentService.getDocumentByID(id);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
