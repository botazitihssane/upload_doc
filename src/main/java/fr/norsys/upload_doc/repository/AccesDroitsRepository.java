package fr.norsys.upload_doc.repository;

import fr.norsys.upload_doc.entity.AccesDroits;
import fr.norsys.upload_doc.entity.AccesDroitsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccesDroitsRepository extends JpaRepository<AccesDroits, AccesDroitsId> {
    void deleteById(AccesDroitsId accesDroitsId);

    long countByAccesId(UUID accesId);

}
