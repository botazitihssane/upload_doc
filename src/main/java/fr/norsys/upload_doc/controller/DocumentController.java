package fr.norsys.upload_doc.controller;


import fr.norsys.upload_doc.dto.DocumentDetailsResponse;
import fr.norsys.upload_doc.dto.DocumentSaveRequest;
import fr.norsys.upload_doc.exception.MetadataNotFoundException;
import fr.norsys.upload_doc.service.DocumentService;
import fr.norsys.upload_doc.service.impl.DocumentServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("api/document")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class DocumentController {

    @Autowired
    private DocumentServiceImpl documentServiceImpl;

    private final DocumentService documentService;

    @PostMapping("/save")
    public ResponseEntity<?> saveDocument(@ModelAttribute DocumentSaveRequest documentSaveRequest, @RequestParam("file") MultipartFile multipartFile) {
        System.out.println("document" + documentSaveRequest);
        return documentServiceImpl.save(documentSaveRequest, multipartFile);
    }

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

    @GetMapping("/search")
    public ResponseEntity<List<DocumentDetailsResponse>> searchDocuments(@RequestParam(required = false, defaultValue = "") String nom,
                                                                         @RequestParam(required = false, defaultValue = "") String type,
                                                                         @RequestParam(required = false) LocalDate date) {
        List<DocumentDetailsResponse> response = documentService.searchDocuments(nom, type, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/metadata")
    public ResponseEntity<?> searchDocumentsByMetaData(@RequestParam(required = false) Map<String, String> metadata) {
        try {
            List<DocumentDetailsResponse> response = documentService.searchDocumentsByMetaData(metadata);
            return ResponseEntity.ok(response);
        } catch (MetadataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

}
