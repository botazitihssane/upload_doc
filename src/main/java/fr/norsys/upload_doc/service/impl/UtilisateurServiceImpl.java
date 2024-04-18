package fr.norsys.upload_doc.service.impl;

import fr.norsys.upload_doc.entity.Utilisateur;
import fr.norsys.upload_doc.repository.UtilisateurRepository;
import fr.norsys.upload_doc.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {
   @Autowired
    public UtilisateurRepository utilisateurRepository;

    public ResponseEntity<?> save(Utilisateur utilisateur) {
        try {
            Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
            return ResponseEntity.ok(savedUtilisateur);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while saving the utilisateur.");
        }
    }

    @Override
    public Optional<Utilisateur> findById(UUID id) {
        return utilisateurRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
  utilisateurRepository.deleteById(id);
    }
}
