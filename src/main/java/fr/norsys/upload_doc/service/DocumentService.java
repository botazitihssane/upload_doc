package fr.norsys.upload_doc.service;


import fr.norsys.upload_doc.dto.DocumentDetailsResponse;

import java.util.UUID;

public interface DocumentService {

    public DocumentDetailsResponse getDocumentByID(UUID id);
}
