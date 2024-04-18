CREATE TABLE acces_droits
(
    acces_id UUID         NOT NULL,
    droits   VARCHAR(255) NOT NULL,
    PRIMARY KEY (acces_id, droits),
    CONSTRAINT fk_acces
        FOREIGN KEY (acces_id)
            REFERENCES acces (id),
    CONSTRAINT check_droits
        CHECK (droits IN ('ECRITURE', 'LECTURE'))
);
