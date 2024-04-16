package fr.norsys.upload_doc.controller;

import fr.norsys.upload_doc.entity.Document;
import fr.norsys.upload_doc.service.impl.DocumentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/document")
public class DocumentController {
    @Autowired
    private DocumentServiceImpl documentServiceImpl;
    @PostMapping("/save")
    public ResponseEntity<?> saveDocument(@ModelAttribute Document document, @RequestParam("file") MultipartFile multipartFile) {
        return documentServiceImpl.save(document, multipartFile);
    }


}
