package fr.norsys.upload_doc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class DocumentDetailsResponse {
    private UUID id;
    private String nom;
    private String type;
    private LocalDate dateCreation;
    private Set<MetadataResponse> metadataResponse;
}