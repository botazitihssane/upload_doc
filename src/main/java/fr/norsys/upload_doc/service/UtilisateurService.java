package fr.norsys.upload_doc.service;

import fr.norsys.upload_doc.entity.Utilisateur;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

public interface UtilisateurService {
    ResponseEntity<?> save(Utilisateur utilisateur);

    Optional<Utilisateur> findById(UUID id);

    void deleteById(UUID id);

}
