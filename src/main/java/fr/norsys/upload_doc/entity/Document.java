package fr.norsys.upload_doc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

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
    private LocalDate dateCreation;

    private String emplacement;
    private String hash;


    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Metadata> metadatas;


    @OneToMany(mappedBy = "idDocument")
    private List<Acces> acces;


    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

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
