package fr.norsys.upload_doc.service;

import fr.norsys.upload_doc.dto.AccesSaveRequest;
import fr.norsys.upload_doc.exception.AccesAlreadyExistException;
import fr.norsys.upload_doc.exception.DocumentNotFound;
import fr.norsys.upload_doc.exception.UserNotFoundException;

public interface AccesService {
    void addAccesToUser(AccesSaveRequest accesSaveRequest) throws UserNotFoundException, DocumentNotFound, AccesAlreadyExistException;
}
