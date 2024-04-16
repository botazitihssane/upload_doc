package fr.norsys.upload_doc.service;


import java.util.UUID;
import fr.norsys.upload_doc.entity.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import fr.norsys.upload_doc.dto.DocumentDetailsResponse;
public interface DocumentService {
    ResponseEntity<?> save(Document document, MultipartFile multipartFile);
    DocumentDetailsResponse getDocumentByID(UUID id);





}
