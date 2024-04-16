package fr.norsys.upload_doc.service;


import fr.norsys.upload_doc.entity.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    ResponseEntity<?> save(Document document, MultipartFile multipartFile);
}
