package fr.norsys.upload_doc.service.impl;

import fr.norsys.upload_doc.dto.AccesSaveRequest;
import fr.norsys.upload_doc.entity.Acces;
import fr.norsys.upload_doc.entity.Document;
import fr.norsys.upload_doc.entity.Utilisateur;
import fr.norsys.upload_doc.enumeration.Droit;
import fr.norsys.upload_doc.exception.DocumentNotFound;
import fr.norsys.upload_doc.exception.UserNotFoundException;
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

    @Override
    public void addAccesToUser(AccesSaveRequest accesSaveRequest) throws UserNotFoundException, DocumentNotFound {
        Utilisateur utilisateur = Optional.of(utilisateurRepository.findByEmail(accesSaveRequest.email()))
                .orElseThrow(() -> new UserNotFoundException(accesSaveRequest.email()));

        Document document = documentRepository.findById(UUID.fromString(accesSaveRequest.docId()))
                .orElseThrow(() -> new DocumentNotFound(accesSaveRequest.docId()));

        Set<Droit> droits = accesSaveRequest.droits();
        Acces acces = new Acces();
        acces.setIdDocument(document);
        acces.setIdUtilisateur(utilisateur);
        acces.setDroits(droits);
        accesRepository.save(acces);
    }

}
