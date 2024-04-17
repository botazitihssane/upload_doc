package fr.norsys.upload_doc.dto;

import org.springframework.web.bind.annotation.RequestParam;

public record MetaDataSaveRequest(@RequestParam("key") String cle,
                                  @RequestParam("value") String valeur) {
}
