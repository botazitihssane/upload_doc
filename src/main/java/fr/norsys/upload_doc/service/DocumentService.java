package fr.norsys.upload_doc.service;


import java.util.UUID;
import fr.norsys.upload_doc.entity.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import fr.norsys.upload_doc.dto.DocumentDetailsResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface DocumentService {
    ResponseEntity<?> save(Document document, MultipartFile multipartFile);
   public DocumentDetailsResponse getDocumentByID(UUID id);

    public DocumentDetailsResponse getDocumentByID(UUID id);

    public List<DocumentDetailsResponse> searchDocuments(String nom, String type, LocalDate date);

    public List<DocumentDetailsResponse> searchDocumentsByMetaData(Map<String, String> metadatas);

}
