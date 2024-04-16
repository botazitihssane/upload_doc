package fr.norsys.upload_doc.controller;

import fr.norsys.upload_doc.dto.DocumentDetailsResponse;
import fr.norsys.upload_doc.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("api/document")
@AllArgsConstructor
public class DocumentController {

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

    @GetMapping("/search")
    public ResponseEntity<List<DocumentDetailsResponse>> searchDocuments(@RequestParam(required = false, defaultValue = "") String nom,
                                                                         @RequestParam(required = false, defaultValue = "") String type,
                                                                         @RequestParam(required = false) LocalDate date) {
        List<DocumentDetailsResponse> response = documentService.searchDocuments(nom, type, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/metadata")
    public ResponseEntity<List<DocumentDetailsResponse>> searchDocumentsByMetaData(@RequestParam(required = false) Map<String, String> metadata) {
        List<DocumentDetailsResponse> response = documentService.searchDocumentsByMetaData(metadata);
        return ResponseEntity.ok(response);
    }
}
