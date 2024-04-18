package fr.norsys.upload_doc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class DocumentSaveResponse {

    private String nom;
    private String type;
    private LocalDate dateCreation;
    private String emplacement;
    private Set<MetadataResponse> metadataResponse;
}
