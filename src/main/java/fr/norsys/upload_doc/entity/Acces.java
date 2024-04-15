package fr.norsys.upload_doc.entity;

import fr.norsys.upload_doc.enumeration.Droit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Enumerated(EnumType.STRING)
    private Droit droits;
}
