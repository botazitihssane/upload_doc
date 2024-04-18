package fr.norsys.upload_doc.dto;

import fr.norsys.upload_doc.enumeration.Droit;

import java.util.List;
import java.util.Set;

public record AccesSaveRequest(String email, String docId, Set<Droit> droits) {
}
