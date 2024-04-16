package fr.norsys.upload_doc.service.impl;


import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import fr.norsys.upload_doc.entity.Document;
import fr.norsys.upload_doc.entity.Metadata;
import fr.norsys.upload_doc.repository.DocumentRepository;

import fr.norsys.upload_doc.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.norsys.upload_doc.dto.DocumentDetailsResponse;
import fr.norsys.upload_doc.dto.MetadataResponse;

import fr.norsys.upload_doc.repository.MetadataRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {

   @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private MetadataRepository metadataRepository;

    private String uploadFile(File file, String fileName) throws IOException {
        String contentType = getContentType(fileName);

        BlobId blobId = BlobId.of("uploaddoc-a26b9.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        InputStream inputStream = DocumentServiceImpl.class.getClassLoader().getResourceAsStream("uploaddoc-firebase-adminsdk.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/uploaddoc-a26b9.appspot.com/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }
    private String getContentType(String fileName) {

        String extension = getExtension(fileName).toLowerCase();
        switch (extension) {
            case ".pdf":
                return "application/pdf";
            case ".doc":
            case ".docx":
                return "application/msword";
            case ".xls":
            case ".xlsx":
                return "application/vnd.ms-excel";
            case ".txt":
                return "text/plain";
            default:
                return "application/octet-stream";
        }
    }

    @Override
    public ResponseEntity<?> save(Document document, MultipartFile multipartFile)  {

        try {

            String fileHash = calculateHash(multipartFile);
            System.out.println("file hash"+ fileHash);

          for (Document existingDocument : documentRepository.findAll()) {
              String existingFileHash = existingDocument.getHash();
                System.out.println("existing file hash"+ existingFileHash);

                if (existingFileHash.equals(fileHash)) {
                    return ResponseEntity.status(HttpStatus.OK).body("File already exists. URL: " + existingDocument.getEmplacement());
                }
            }



            String fileName = multipartFile.getOriginalFilename();
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));
            File file = this.convertToFile(multipartFile, fileName);
            String URL = this.uploadFile(file, fileName);
            file.delete();


             document.setEmplacement(URL);
             document.setHash(fileHash);
             documentRepository.save(document);


            for (Metadata meta : document.getMetadatas()) {

                meta.setCle(meta.getCle());
                meta.setValeur(meta.getValeur());
                meta.setDocument(document);

                metadataRepository.save(meta);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Document saved successfully. URL: " + document.getEmplacement());

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the document.");

        }
    }
    private String calculateHash(MultipartFile file) {

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(file.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }








    @Override
    public DocumentDetailsResponse getDocumentByID(UUID id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No document with the specified id"));

        Set<Metadata> metadataSet = metadataRepository.getMetadataByDocumentId(id);
        if (metadataSet.isEmpty()) {
            throw new NoSuchElementException("No metadata found for the document with the specified id");
        }

        Set<MetadataResponse> metadataResponses = metadataSet.stream()
                .map(metadata -> new MetadataResponse(metadata.getCle(), metadata.getValeur()))
                .collect(Collectors.toSet());

        return new DocumentDetailsResponse(document.getNom(), document.getType(), document.getDateCreation(), metadataResponses);
    }

}
