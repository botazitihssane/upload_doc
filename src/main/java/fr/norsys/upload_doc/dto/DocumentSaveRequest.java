package fr.norsys.upload_doc.dto;

import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;

public record DocumentSaveRequest(@RequestParam("nom") String nom,
                                  @RequestParam("type") String type,
                                  @RequestParam("dateCreation") LocalDate dateCreation,
                                  @RequestParam("metadata") Map<String, String> metadata) {
}
