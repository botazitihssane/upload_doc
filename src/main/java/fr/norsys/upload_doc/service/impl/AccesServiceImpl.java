package fr.norsys.upload_doc.service.impl;

import fr.norsys.upload_doc.dto.AccesSaveRequest;
import fr.norsys.upload_doc.entity.Acces;
import fr.norsys.upload_doc.entity.Document;
import fr.norsys.upload_doc.entity.Utilisateur;
import fr.norsys.upload_doc.enumeration.Droit;
import fr.norsys.upload_doc.exception.AccesAlreadyExistException;
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
    public void addAccesToUser(AccesSaveRequest accesSaveRequest) throws UserNotFoundException, DocumentNotFound, AccesAlreadyExistException {
        Utilisateur utilisateur = Optional.of(utilisateurRepository.findByEmail(accesSaveRequest.email()))
                .orElseThrow(() -> new UserNotFoundException(accesSaveRequest.email()));

        Document document = documentRepository.findById(UUID.fromString(accesSaveRequest.docId()))
                .orElseThrow(() -> new DocumentNotFound(accesSaveRequest.docId()));

        Set<Droit> requestedDroits = accesSaveRequest.droits();

        Optional<List<Acces>> existingAccesOpt = accesRepository.findExistingAcces(document.getId(), utilisateur.getId(), requestedDroits);

        System.out.println(accesSaveRequest);

        if (existingAccesOpt.isPresent() && !existingAccesOpt.get().isEmpty()) {
            List<Acces> accesList = existingAccesOpt.get();
            System.out.println("Entered with non-empty existingAccesOpt: " + accesList);

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
            System.out.println("Entered with empty existingAccesOpt");
            Acces newAcces = new Acces();
            newAcces.setIdDocument(document);
            newAcces.setIdUtilisateur(utilisateur);
            newAcces.setDroits(requestedDroits);
            accesRepository.save(newAcces);
        }
    }

}
