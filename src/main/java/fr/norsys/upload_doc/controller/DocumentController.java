package fr.norsys.upload_doc.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.norsys.upload_doc.dto.DocumentDetailsResponse;

import fr.norsys.upload_doc.dto.DocumentSaveResponse;
import fr.norsys.upload_doc.dto.MetadataResponse;
import fr.norsys.upload_doc.entity.Document;
import fr.norsys.upload_doc.entity.Metadata;

import fr.norsys.upload_doc.dto.DocumentSaveRequest;

import fr.norsys.upload_doc.exception.MetadataNotFoundException;
import fr.norsys.upload_doc.service.DocumentService;
import fr.norsys.upload_doc.service.impl.DocumentServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/document")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class DocumentController {

    @Autowired
    private DocumentServiceImpl documentServiceImpl;
    @Autowired
    private  DocumentService documentService;

    @PostMapping("/save")
    public ResponseEntity<?> saveDocument(@RequestParam("nom") String nom,
                                          @RequestParam("type") String type,
                                          @RequestParam("dateCreation") LocalDate dateCreation,
                                          @RequestParam("metadata") String metadataJson,
                                          @RequestParam("file") MultipartFile multipartFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> metadata;
        try {
            metadata = objectMapper.readValue(metadataJson, new TypeReference<Map<String, String>>() {
            });
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid metadata JSON");
        }

        DocumentSaveRequest documentSaveRequest = new DocumentSaveRequest(nom, type, dateCreation, metadata);
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
    public ResponseEntity<List<DocumentDetailsResponse>> searchDocuments(@RequestParam(required = false, defaultValue = "") String nom, @RequestParam(required = false, defaultValue = "") String type, @RequestParam(required = false) LocalDate date) {
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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {

            documentServiceImpl.deleteById(id);


    }
   

}
