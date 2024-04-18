package fr.norsys.upload_doc.service;


import fr.norsys.upload_doc.dto.DocumentDetailsResponse;
import fr.norsys.upload_doc.entity.Document;
import fr.norsys.upload_doc.entity.Metadata;
import fr.norsys.upload_doc.dto.DocumentSaveRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface DocumentService {
    ResponseEntity<?> save(DocumentSaveRequest documentSaveRequest, MultipartFile multipartFile);

    DocumentDetailsResponse getDocumentByID(UUID id);

void deleteById(UUID id);
    public List<DocumentDetailsResponse> searchDocuments(String nom, String type, LocalDate date);

    public List<DocumentDetailsResponse> searchDocumentsByMetaData(Map<String, String> metadatas);

}
