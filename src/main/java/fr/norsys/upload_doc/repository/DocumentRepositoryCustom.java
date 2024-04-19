package fr.norsys.upload_doc.repository;

import fr.norsys.upload_doc.entity.Document;
import fr.norsys.upload_doc.entity.Utilisateur;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DocumentRepositoryCustom {
    List<Document> searchDocuments(String nom, String type, LocalDate dateCreation, Utilisateur utilisateur);

    List<Document> searchDocumentsByMetaData(Map<String, String> metadatas);

}
