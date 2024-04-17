package fr.norsys.upload_doc.repository;

import fr.norsys.upload_doc.entity.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DocumentRepositoryCustom {
    List<Document> searchDocuments(String nom, String type, LocalDate dateCreation);

    List<Document> searchDocumentsByMetaData(Map<String, String> metadatas);

}
