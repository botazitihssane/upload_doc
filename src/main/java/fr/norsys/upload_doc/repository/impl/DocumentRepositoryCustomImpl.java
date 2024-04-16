package fr.norsys.upload_doc.repository.impl;

import fr.norsys.upload_doc.entity.Document;
import fr.norsys.upload_doc.entity.Metadata;
import fr.norsys.upload_doc.repository.DocumentRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class DocumentRepositoryCustomImpl implements DocumentRepositoryCustom {

    private final EntityManager entityManager;

    private void addNomPredicate(CriteriaBuilder criteriaBuilder, Root<Document> documentRoot, List<Predicate> predicates, String nom) {
        if (nom != null && !nom.isEmpty()) {
            Predicate nomPredicate = criteriaBuilder.like(criteriaBuilder.lower(documentRoot.get("nom")), "%" + nom.toLowerCase() + "%");
            predicates.add(nomPredicate);
        }
    }

    private void addTypePredicate(CriteriaBuilder criteriaBuilder, Root<Document> documentRoot, List<Predicate> predicates, String type) {
        if (type != null && !type.isEmpty()) {
            Predicate typePredicate = criteriaBuilder.like(criteriaBuilder.lower(documentRoot.get("type")), "%" + type.toLowerCase() + "%");
            predicates.add(typePredicate);
        }
    }

    private void addDateCreationPredicate(CriteriaBuilder criteriaBuilder, Root<Document> documentRoot, List<Predicate> predicates, LocalDate dateCreation) {
        if (dateCreation != null) {
            Predicate dateCreationPredicate = criteriaBuilder.equal(documentRoot.get("dateCreation"), dateCreation);
            predicates.add(dateCreationPredicate);
        }
    }

    private void addMetadataPredicates(CriteriaBuilder criteriaBuilder, Root<Document> documentRoot, List<Predicate> predicates, Map<String, String> metadatas) {
        if (metadatas != null && !metadatas.isEmpty()) {
            Join<Document, Metadata> metadataJoin = documentRoot.join("metadatas");
            for (Map.Entry<String, String> entry : metadatas.entrySet()) {
                Predicate keyPredicate = criteriaBuilder.equal(metadataJoin.get("cle"), entry.getKey());
                Predicate valuePredicate = criteriaBuilder.equal(metadataJoin.get("valeur"), entry.getValue());
                predicates.add(criteriaBuilder.and(keyPredicate, valuePredicate));
            }
        }
    }

    @Override
    public List<Document> searchDocuments(String nom, String type, LocalDate dateCreation) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Document> query = criteriaBuilder.createQuery(Document.class);

        Root<Document> documentRoot = query.from(Document.class);

        List<Predicate> predicates = new ArrayList<>();

        addNomPredicate(criteriaBuilder, documentRoot, predicates, nom);
        addTypePredicate(criteriaBuilder, documentRoot, predicates, type);
        addDateCreationPredicate(criteriaBuilder, documentRoot, predicates, dateCreation);

        if (!predicates.isEmpty()) {
            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        }

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<Document> searchDocumentsByMetaData(Map<String, String> metadatas) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Document> query = criteriaBuilder.createQuery(Document.class);

        Root<Document> documentRoot = query.from(Document.class);

        List<Predicate> predicates = new ArrayList<>();

        addMetadataPredicates(criteriaBuilder, documentRoot, predicates, metadatas);

        if (!predicates.isEmpty()) {
            query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        }

        return entityManager.createQuery(query).getResultList();
    }

}
