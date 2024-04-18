package fr.norsys.upload_doc.repository;

import fr.norsys.upload_doc.entity.Acces;
import fr.norsys.upload_doc.enumeration.Droit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface AccesRepository extends JpaRepository<Acces, UUID> {
    @Query("SELECT a FROM Acces a JOIN a.droits d WHERE a.idDocument.id = :documentId AND a.idUtilisateur.id = :userId AND d IN :droits")
    Optional<List<Acces>> findExistingAcces(@Param("documentId") UUID documentId, @Param("userId") UUID userId, @Param("droits") Set<Droit> droits);

}
