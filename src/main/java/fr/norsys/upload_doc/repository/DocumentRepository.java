package fr.norsys.upload_doc.repository;

import fr.norsys.upload_doc.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID>, DocumentRepositoryCustom {

}