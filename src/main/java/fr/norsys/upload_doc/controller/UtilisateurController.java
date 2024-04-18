package fr.norsys.upload_doc.controller;

import fr.norsys.upload_doc.dto.UtilisateurSaveRequest;
import fr.norsys.upload_doc.entity.Utilisateur;
import fr.norsys.upload_doc.service.UtilisateurService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("api/user")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody UtilisateurSaveRequest utilisateurSaveRequest) {
        utilisateurService.save(utilisateurSaveRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public Optional<Utilisateur> findById(@PathVariable UUID id) {
        return utilisateurService.findById(id);
    }

    @GetMapping("/email/{email}")
    public Optional<Utilisateur> findByEmail(@PathVariable String email) {
        return utilisateurService.findByEmail(email);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        utilisateurService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
