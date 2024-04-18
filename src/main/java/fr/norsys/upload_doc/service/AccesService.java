package fr.norsys.upload_doc.service;

import fr.norsys.upload_doc.dto.AccesRequest;
import fr.norsys.upload_doc.exception.AccesAlreadyExistException;
import fr.norsys.upload_doc.exception.AccesNotFoundException;
import fr.norsys.upload_doc.exception.DocumentNotFound;
import fr.norsys.upload_doc.exception.UserNotFoundException;

public interface AccesService {
    void addAccesToUser(AccesRequest accesRequest) throws UserNotFoundException, DocumentNotFound, AccesAlreadyExistException;

    void revokeAcces(AccesRequest accesRequest) throws UserNotFoundException, DocumentNotFound, AccesNotFoundException;
}
