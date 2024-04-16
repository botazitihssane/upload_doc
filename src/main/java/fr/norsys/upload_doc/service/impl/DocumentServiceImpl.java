package fr.norsys.upload_doc.service.impl;

import fr.norsys.upload_doc.dto.DocumentDetailsResponse;
import fr.norsys.upload_doc.dto.MetadataResponse;
import fr.norsys.upload_doc.entity.Document;
import fr.norsys.upload_doc.entity.Metadata;
import fr.norsys.upload_doc.exception.MetadataNotFoundException;
import fr.norsys.upload_doc.repository.DocumentRepository;
import fr.norsys.upload_doc.repository.MetadataRepository;
import fr.norsys.upload_doc.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final MetadataRepository metadataRepository;

    @Override
    public DocumentDetailsResponse getDocumentByID(UUID id) {
        Document document = documentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No document with the specified id"));

        return mapToDTOResponse(document);
    }

    @Override
    public List<DocumentDetailsResponse> searchDocuments(String nom, String type, LocalDate date) {
        List<Document> documents = documentRepository.searchDocuments(nom, type, date);
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

        return new DocumentDetailsResponse(document.getNom(), document.getType(), document.getDateCreation(), metadataResponses);
    }
}
