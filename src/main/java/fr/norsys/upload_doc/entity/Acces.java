package fr.norsys.upload_doc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Acces {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    private Document idDocument;
    @ManyToOne
    private Utilisateur idUtilisateur;

    @OneToMany(mappedBy = "acces")
    private Set<AccesDroits> accesDroits;
}
