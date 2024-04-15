package fr.norsys.upload_doc.service.impl;

import fr.norsys.upload_doc.dto.DocumentDetailsResponse;
import fr.norsys.upload_doc.dto.MetadataResponse;
import fr.norsys.upload_doc.entity.Document;
import fr.norsys.upload_doc.entity.Metadata;
import fr.norsys.upload_doc.repository.DocumentRepository;
import fr.norsys.upload_doc.repository.MetadataRepository;
import fr.norsys.upload_doc.service.DocumentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final MetadataRepository metadataRepository;

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
