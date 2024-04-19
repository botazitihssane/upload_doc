create table document
(
    id            uuid not null
        primary key,
    date_creation timestamp(6),
    nom           varchar(255),
    type          varchar(255)
);


create table metadata
(
    id          uuid not null
        primary key,
    cle         varchar(255),
    valeur      varchar(255),
    document_id uuid
        constraint fk2iyht7aw808inc4q1aqsm9x5s
            references document
);


create table utilisateur
(
    id    uuid not null
        primary key,
    email varchar(255)
);

create table acces
(
    id                uuid not null
        primary key,
    id_document_id    uuid
        constraint fkg1lscw2kt20me4l5uxcvjykp2
            references document,
    id_utilisateur_id uuid
        constraint fk6ooubb1wh2s3hrkkyfks447ej
            references utilisateur
);

