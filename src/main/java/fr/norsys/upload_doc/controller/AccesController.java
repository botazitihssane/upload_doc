package fr.norsys.upload_doc.controller;

import fr.norsys.upload_doc.dto.AccesSaveRequest;
import fr.norsys.upload_doc.exception.AccesAlreadyExistException;
import fr.norsys.upload_doc.exception.DocumentNotFound;
import fr.norsys.upload_doc.exception.UserNotFoundException;
import fr.norsys.upload_doc.service.AccesService;
import lombok.AllArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/acces")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class AccesController {
    private final AccesService accesService;

    @PostMapping
    ResponseEntity<?> addAcces(@RequestBody AccesSaveRequest accesSaveRequest) throws UserNotFoundException, DocumentNotFound {
        try {
            accesService.addAccesToUser(accesSaveRequest);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException | DocumentNotFound e) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(e.getMessage());
        } catch (AccesAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
