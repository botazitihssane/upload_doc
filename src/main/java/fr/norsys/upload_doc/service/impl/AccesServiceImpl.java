package fr.norsys.upload_doc.service.impl;

import fr.norsys.upload_doc.dto.AccesRequest;
import fr.norsys.upload_doc.entity.Acces;
import fr.norsys.upload_doc.entity.Document;
import fr.norsys.upload_doc.entity.Utilisateur;
import fr.norsys.upload_doc.enumeration.Droit;
import fr.norsys.upload_doc.exception.AccesAlreadyExistException;
import fr.norsys.upload_doc.exception.AccesNotFoundException;
import fr.norsys.upload_doc.exception.DocumentNotFound;
import fr.norsys.upload_doc.exception.UserNotFoundException;
import fr.norsys.upload_doc.repository.AccesRepository;
import fr.norsys.upload_doc.repository.DocumentRepository;
import fr.norsys.upload_doc.repository.UtilisateurRepository;
import fr.norsys.upload_doc.service.AccesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class AccesServiceImpl implements AccesService {

    private final AccesRepository accesRepository;

    private final DocumentRepository documentRepository;

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public void addAccesToUser(AccesRequest accesRequest) throws UserNotFoundException, DocumentNotFound, AccesAlreadyExistException {
        Utilisateur utilisateur = retrieveUtilisateur(accesRequest.email());
        Document document = retrieveDocument(accesRequest.docId());

        Set<Droit> requestedDroits = accesRequest.droits();

        Optional<List<Acces>> existingAccesOpt = accesRepository.findExistingAcces(document.getId(), utilisateur.getId(), requestedDroits);

        if (existingAccesOpt.isPresent() && !existingAccesOpt.get().isEmpty()) {
            List<Acces> accesList = existingAccesOpt.get();

            for (Acces existingAcces : accesList) {
                Set<Droit> existingDroits = existingAcces.getDroits();
                Set<Droit> missingDroits = new HashSet<>(requestedDroits);
                missingDroits.removeAll(existingDroits);

                if (!missingDroits.isEmpty()) {
                    existingAcces.getDroits().addAll(missingDroits);
                    accesRepository.save(existingAcces);
                }
            }
        } else {
            Acces newAcces = new Acces();
            newAcces.setIdDocument(document);
            newAcces.setIdUtilisateur(utilisateur);
            newAcces.setDroits(requestedDroits);
            accesRepository.save(newAcces);
        }
    }

    @Override
    public void revokeAcces(AccesRequest accesRequest) throws UserNotFoundException, DocumentNotFound, AccesNotFoundException {
        Utilisateur utilisateur = retrieveUtilisateur(accesRequest.email());
        Document document = retrieveDocument(accesRequest.docId());

        Set<Droit> revokedDroits = accesRequest.droits();

        Optional<List<Acces>> existingAccesOpt = accesRepository.findExistingAcces(document.getId(), utilisateur.getId(), revokedDroits);

        if (existingAccesOpt.get().isEmpty()) throw new AccesNotFoundException();
        else {
            List<Acces> acces = existingAccesOpt.get();
            boolean accessRemoved = false;

            for (Acces existingAcces : acces) {
                Set<Droit> existingDroits = existingAcces.getDroits();
                existingDroits.removeAll(revokedDroits);

                if (existingDroits.isEmpty()) {
                    accesRepository.delete(existingAcces);
                } else {
                    accesRepository.save(existingAcces);
                }
                accessRemoved = true;
            }

        }
    }

    private Utilisateur retrieveUtilisateur(String email) throws UserNotFoundException {
        return Optional.of(utilisateurRepository.findByEmail(email)).orElseThrow(() -> new UserNotFoundException(email));
    }

    private Document retrieveDocument(String docId) throws DocumentNotFound {
        return documentRepository.findById(UUID.fromString(docId)).orElseThrow(() -> new DocumentNotFound(docId));
    }

}
