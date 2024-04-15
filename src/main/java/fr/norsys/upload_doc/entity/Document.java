package fr.norsys.upload_doc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)

    private UUID id;
    private String nom;
    private String type;
    private Date dateCreation;

    @OneToMany
    private List<MetaData> metadatas;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document)) return false;
        Document document = (Document) o;
        return nom.equals(document.nom) && type.equals(document.type) && dateCreation.equals(document.dateCreation) && metadatas.equals(document.metadatas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, type, dateCreation, metadatas);
    }



}
