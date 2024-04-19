package fr.norsys.upload_doc.service.impl;

import fr.norsys.upload_doc.dto.AccesRequest;
import fr.norsys.upload_doc.entity.*;
import fr.norsys.upload_doc.enumeration.Droit;
import fr.norsys.upload_doc.exception.AccesAlreadyExistException;
import fr.norsys.upload_doc.exception.AccesNotFoundException;
import fr.norsys.upload_doc.exception.DocumentNotFound;
import fr.norsys.upload_doc.exception.UserNotFoundException;
import fr.norsys.upload_doc.repository.AccesDroitsRepository;
import fr.norsys.upload_doc.repository.AccesRepository;
import fr.norsys.upload_doc.repository.DocumentRepository;
import fr.norsys.upload_doc.repository.UtilisateurRepository;
import fr.norsys.upload_doc.service.AccesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccesServiceImpl implements AccesService {

    private final AccesRepository accesRepository;

    private final DocumentRepository documentRepository;

    private final UtilisateurRepository utilisateurRepository;

    private final AccesDroitsRepository accesDroitsRepository;

    @Override
    public void addAccesToUser(AccesRequest accesRequest) throws UserNotFoundException, DocumentNotFound, AccesAlreadyExistException {
        Utilisateur utilisateur = retrieveUtilisateur(accesRequest.email());
        Document document = retrieveDocument(accesRequest.docId());

        Optional<Acces> existingAcces = accesRepository.findByIdDocumentAndIdUtilisateur(document, utilisateur);
        if (existingAcces.isPresent()) {
            throw new AccesAlreadyExistException();
        }

        Acces acces = new Acces();
        acces.setIdDocument(document);
        acces.setIdUtilisateur(utilisateur);
        accesRepository.save(acces);

        Set<Droit> requestedDroits = accesRequest.droits();
        System.out.println(requestedDroits);

        for (Droit droit : requestedDroits) {
            AccesDroitsId accesDroitsId = new AccesDroitsId(acces.getId(), droit);
            AccesDroits accesDroits = new AccesDroits(accesDroitsId, acces, utilisateur);
            accesDroitsRepository.save(accesDroits);
        }
    }


    @Override
    public void revokeAcces(AccesRequest accesRequest) throws UserNotFoundException, DocumentNotFound, AccesNotFoundException {
        Utilisateur utilisateur = retrieveUtilisateur(accesRequest.email());
        Document document = retrieveDocument(accesRequest.docId());

        Optional<Acces> existingAccesOpt = accesRepository.findByIdDocumentAndIdUtilisateur(document, utilisateur);
        if (existingAccesOpt.isPresent()) {
            Acces acces = existingAccesOpt.get();
            Set<Droit> revokedDroits = accesRequest.droits();

            for (Droit droit : revokedDroits) {
                AccesDroitsId accesDroitsId = new AccesDroitsId(acces.getId(), droit);
                accesDroitsRepository.deleteById(accesDroitsId);
            }

            long remainingDroitsCount = accesDroitsRepository.countByAccesId(acces.getId());
            if (remainingDroitsCount == 0) {
                accesRepository.delete(acces);
            }
        } else {
            throw new AccesNotFoundException();
        }
    }

    private Utilisateur retrieveUtilisateur(String email) throws UserNotFoundException {
        return Optional.of(utilisateurRepository.findByEmail(email)).orElseThrow(() -> new UserNotFoundException(email));
    }

    private Document retrieveDocument(String docId) throws DocumentNotFound {
        return documentRepository.findById(UUID.fromString(docId)).orElseThrow(() -> new DocumentNotFound(docId));
    }

}
