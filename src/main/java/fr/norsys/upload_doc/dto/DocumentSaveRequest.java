package fr.norsys.upload_doc.dto;

import java.util.Map;

public record DocumentSaveRequest(String nom, String type, String email, Map<String, String> metadata) {

}
