package fr.norsys.upload_doc.dto;

import fr.norsys.upload_doc.entity.Metadata;

import java.util.Date;
import java.util.List;
import java.util.Map;

public record DocumentSaveRequest(String nom, String type, Date dateCreation, Map<String,String> metadatas) {
}
