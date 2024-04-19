package fr.norsys.upload_doc.repository;

import fr.norsys.upload_doc.entity.Acces;
import fr.norsys.upload_doc.entity.Document;
import fr.norsys.upload_doc.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccesRepository extends JpaRepository<Acces, UUID> {
    Optional<Acces> findByIdDocumentAndIdUtilisateur(Document document, Utilisateur utilisateur);

}
