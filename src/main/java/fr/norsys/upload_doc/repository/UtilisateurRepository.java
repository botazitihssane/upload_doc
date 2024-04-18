package fr.norsys.upload_doc.repository;

import fr.norsys.upload_doc.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, UUID> {
    Utilisateur findByEmail(String email);
}
