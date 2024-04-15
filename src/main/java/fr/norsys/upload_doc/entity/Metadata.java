package fr.norsys.upload_doc.entity;

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
public class Metadata {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String cle;

    private String valeur;

    @ManyToOne
    private Document document;
}
