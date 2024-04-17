package fr.norsys.upload_doc.repository;

import fr.norsys.upload_doc.entity.Document;
import fr.norsys.upload_doc.entity.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface MetadataRepository extends JpaRepository<Metadata, UUID> {
}
