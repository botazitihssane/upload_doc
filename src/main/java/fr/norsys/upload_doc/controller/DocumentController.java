package fr.norsys.upload_doc.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.norsys.upload_doc.dto.DocumentDetailsResponse;
import fr.norsys.upload_doc.dto.DocumentSaveRequest;
import fr.norsys.upload_doc.exception.MetadataNotFoundException;
import fr.norsys.upload_doc.exception.UserNotFoundException;
import fr.norsys.upload_doc.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("api/document")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/save")
    public ResponseEntity<?> saveDocument(@RequestParam("nom") String nom, @RequestParam("type") String type, @RequestParam(value = "metadata", required = false) String metadataJson, @RequestParam("email") String email, @RequestParam("file") MultipartFile multipartFile) {

        Map<String, String> metadata = new HashMap<>();

        if (metadataJson != null && !metadataJson.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                metadata = objectMapper.readValue(metadataJson, new TypeReference<Map<String, String>>() {
                });
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid metadata JSON");
            }
        }

        DocumentSaveRequest documentSaveRequest = new DocumentSaveRequest(nom, type, email, metadata);
        return documentService.save(documentSaveRequest, multipartFile);
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
    public ResponseEntity<List<DocumentDetailsResponse>> searchDocuments(@RequestParam(required = false, defaultValue = "") String nom, @RequestParam(required = false, defaultValue = "") String type, @RequestParam(required = false) LocalDate date, @RequestParam(required = true) String email) throws UserNotFoundException {
        List<DocumentDetailsResponse> response = documentService.searchDocuments(nom, type, date, email);
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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        documentService.deleteById(id);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadDocumentById(@PathVariable UUID id) throws IOException {
        return documentService.downloadDocumentById(id);
    }

}



