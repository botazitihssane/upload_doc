package fr.norsys.upload_doc.entity;

import fr.norsys.upload_doc.enumeration.Droit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(AccesDroitsId.class)
public class AccesDroits {
    @Id
    private UUID acces_id;

    @Id
    @Enumerated(EnumType.STRING)
    private Droit droits;

    @ManyToOne
    @JoinColumn(name = "acces_id", insertable = false, updatable = false)
    private Acces acces;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;

    public AccesDroits(AccesDroitsId id, Acces acces, Utilisateur utilisateur) {
        this.acces_id = id.getAcces_id();
        this.droits = id.getDroits();
        this.acces = acces;
        this.utilisateur = utilisateur;
    }

}
