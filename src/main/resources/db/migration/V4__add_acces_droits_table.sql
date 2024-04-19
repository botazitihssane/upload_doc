create table acces_droits
(
    acces_id       uuid         not null
        references acces,
    droits         varchar(255) not null
        constraint check_droits
            check ((droits)::text = ANY
        ((ARRAY ['ECRITURE'::character varying, 'LECTURE'::character varying])::text[])),
    utilisateur_id uuid
        references utilisateur,
    primary key (acces_id, droits)
);