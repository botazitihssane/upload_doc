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
public class MetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String key;

    private String value;

    @ManyToOne

    private Document document;
}
