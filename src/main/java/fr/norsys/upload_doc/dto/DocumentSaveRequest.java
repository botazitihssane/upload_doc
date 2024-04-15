package fr.norsys.upload_doc.dto;

import fr.norsys.upload_doc.entity.MetaData;

import java.util.Date;
import java.util.List;

public record DocumentSaveRequest(String nom, String type, Date dateCreation, List<MetaData> metadatas) {
}
