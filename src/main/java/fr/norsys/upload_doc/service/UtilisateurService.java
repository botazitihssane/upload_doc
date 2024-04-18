package fr.norsys.upload_doc.service;

import fr.norsys.upload_doc.dto.UtilisateurSaveRequest;
import fr.norsys.upload_doc.entity.Utilisateur;

import java.util.Optional;
import java.util.UUID;

public interface UtilisateurService {
    void save(UtilisateurSaveRequest utilisateur);

    Optional<Utilisateur> findById(UUID id);

    void deleteById(UUID id);

    Optional<Utilisateur> findByEmail(String email);
}
