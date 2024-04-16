package fr.norsys.upload_doc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MetadataResponse {
    private String cle;
    private String valeur;
}

