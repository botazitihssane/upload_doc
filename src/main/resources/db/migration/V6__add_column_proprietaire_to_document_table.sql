ALTER TABLE Document
    ADD COLUMN utilisateur_id UUID;

ALTER TABLE Document
    ADD CONSTRAINT fk_document_utilisateur
        FOREIGN KEY (utilisateur_id)
            REFERENCES Utilisateur (id);