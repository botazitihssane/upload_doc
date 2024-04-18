package fr.norsys.upload_doc.service.impl;

import fr.norsys.upload_doc.dto.UtilisateurSaveRequest;
import fr.norsys.upload_doc.entity.Utilisateur;
import fr.norsys.upload_doc.repository.UtilisateurRepository;
import fr.norsys.upload_doc.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {
    @Autowired
    public UtilisateurRepository utilisateurRepository;

    public void save(UtilisateurSaveRequest utilisateur) {
        Utilisateur user = new Utilisateur();
        user.setEmail(utilisateur.email());
        utilisateurRepository.save(user);
    }

    @Override
    public Optional<Utilisateur> findById(UUID id) {
        return utilisateurRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
        utilisateurRepository.deleteById(id);
    }

    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        return Optional.ofNullable(utilisateurRepository.findByEmail(email));
    }
}
