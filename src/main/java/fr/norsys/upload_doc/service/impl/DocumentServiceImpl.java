package fr.norsys.upload_doc.service.impl;


import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import fr.norsys.upload_doc.dto.DocumentDetailsResponse;
import fr.norsys.upload_doc.dto.DocumentSaveRequest;
import fr.norsys.upload_doc.dto.MetadataResponse;
import fr.norsys.upload_doc.entity.Document;
import fr.norsys.upload_doc.entity.Metadata;
import fr.norsys.upload_doc.entity.Utilisateur;
import fr.norsys.upload_doc.exception.MetadataNotFoundException;
import fr.norsys.upload_doc.exception.UserNotFoundException;
import fr.norsys.upload_doc.repository.DocumentRepository;
import fr.norsys.upload_doc.repository.MetadataRepository;
import fr.norsys.upload_doc.repository.UtilisateurRepository;
import fr.norsys.upload_doc.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private MetadataRepository metadataRepository;

    private final UtilisateurRepository utilisateurRepository;

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
    public ResponseEntity<?> save(DocumentSaveRequest documentSaveRequest, MultipartFile multipartFile) {
        try {
            String fileHash = calculateHash(multipartFile);
            System.out.println("file hash" + fileHash);

            for (Document existingDocument : documentRepository.findAll()) {
                String existingFileHash = existingDocument.getHash();
                System.out.println("existing file hash" + existingFileHash);

                if (existingFileHash.equals(fileHash)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("File already exists. URL: " + existingDocument.getEmplacement());
                }
            }

            String fileName = multipartFile.getOriginalFilename();
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));
            File file = this.convertToFile(multipartFile, fileName);
            String URL = this.uploadFile(file, fileName);
            file.delete();

            String email = documentSaveRequest.email();
            Utilisateur utilisateur = Optional.of(utilisateurRepository.findByEmail(email)).orElseThrow(() -> new UserNotFoundException(email));
            Document document = new Document();
            document.setNom(documentSaveRequest.nom());
            document.setType(documentSaveRequest.type());
            document.setDateCreation(LocalDate.now());
            document.setEmplacement(URL);
            document.setHash(fileHash);
            document.setUtilisateur(utilisateur);

            documentRepository.save(document);

            if (documentSaveRequest.metadata() != null) {
                Set<Metadata> metadataSet = createMetadataSet(documentSaveRequest.metadata(), document);
                metadataRepository.saveAll(metadataSet);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("Document saved successfully. URL: " + document.getEmplacement());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the document.");

        }
    }

    public Set<Metadata> createMetadataSet(Map<String, String> metadataMap, Document document) {
        System.out.println(metadataMap);
        Set<Metadata> metadataSet = new HashSet<>();

        if (metadataMap != null) {
            for (Map.Entry<String, String> entry : metadataMap.entrySet()) {
                Metadata metadata = new Metadata();
                metadata.setCle(entry.getKey());
                metadata.setValeur(entry.getValue());
                metadata.setDocument(document);
                metadataSet.add(metadata);
            }
        }

        return metadataSet;
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
        Document document = documentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No document with the specified id"));

        return mapToDTOResponse(document);
    }

    @Override
    public void deleteById(UUID id) {
        documentRepository.deleteById(id);
    }


    @Override
    public List<DocumentDetailsResponse> searchDocuments(String nom, String type, LocalDate date, String email) throws UserNotFoundException {
        Utilisateur utilisateur = Optional.ofNullable(utilisateurRepository.findByEmail(email)).orElseThrow(() -> new UserNotFoundException(email));
        List<Document> documents = documentRepository.searchDocuments(nom, type, date, utilisateur);
        return documents.stream().map(this::mapToDTOResponse).collect(Collectors.toList());
    }

    @Override
    public List<DocumentDetailsResponse> searchDocumentsByMetaData(Map<String, String> metadataFilters) {
        for (String key : metadataFilters.keySet()) {
            boolean exists = metadataRepository.existsByCle(key);
            if (!exists) {
                throw new MetadataNotFoundException(key);
            }
        }
        List<Document> documents = documentRepository.searchDocumentsByMetaData(metadataFilters);
        List<DocumentDetailsResponse> documentDetailsResponses = documents.stream().map(this::mapToDTOResponse).collect(Collectors.toList());
        return documentDetailsResponses;
    }


    private DocumentDetailsResponse mapToDTOResponse(Document document) {
        Set<Metadata> metadataSet = metadataRepository.getMetadataByDocumentId(document.getId());
        Set<MetadataResponse> metadataResponses = metadataSet.stream().map(metadata -> new MetadataResponse(metadata.getCle(), metadata.getValeur())).collect(Collectors.toSet());

        return new DocumentDetailsResponse(document.getId(), document.getNom(), document.getType(), document.getDateCreation(), metadataResponses);
    }

    private String extractFileNameFromUrl(String url) {
        try {
            URL parsedUrl = new URL(url);
            String path = parsedUrl.getPath();
            return Paths.get(path).getFileName().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return ""; // No extension found or the dot is at the end of the file name
        }
        return fileName.substring(dotIndex + 1);
    }

    public ResponseEntity<Resource> downloadDocumentById(UUID id) throws IOException {
        Optional<Document> documentFounded = documentRepository.findById(id);
        if (documentFounded.isPresent()) {
            Document document = documentFounded.get();
            String documentUrl = document.getEmplacement();
            if (documentUrl != null && !documentUrl.isEmpty()) {
                String extractedFileName = extractFileNameFromUrl(documentUrl);
                String fileExtension = getFileExtension(extractedFileName);
                System.out.println("file extention " + fileExtension);
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("uploaddoc-firebase-adminsdk.json");
                GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);

                Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

                String bucketName = "uploaddoc-a26b9.appspot.com";


                Blob blob = storage.get(BlobId.of(bucketName, extractedFileName));
                System.out.println(blob);

                if (blob == null) {
                    return ResponseEntity.notFound().build();
                }


                byte[] fileContent = blob.getContent();


                Resource resource = new ByteArrayResource(fileContent);


                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment", extractedFileName);
                headers.add("File-Extension", fileExtension);

                headers.add("Access-Control-Expose-Headers", "File-Extension, Content-Disposition, Content-Type");

                return ResponseEntity.ok().headers(headers).body(resource);

            }
        }
        return null;

    }


}
