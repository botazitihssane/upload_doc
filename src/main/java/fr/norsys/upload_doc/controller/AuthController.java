package fr.norsys.upload_doc.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import fr.norsys.upload_doc.dto.SignInRequest;
import fr.norsys.upload_doc.dto.SignUpRequest;
import fr.norsys.upload_doc.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        try {
            String message = authService.signUpUser(signUpRequest);
            return ResponseEntity.ok(message);
        } catch (FirebaseAuthException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/signin")
    public ResponseEntity<String> signInUser(@RequestBody SignInRequest signInRequest) {
        try {
            String message = authService.signInUser(signInRequest);
            if ("User authenticated successfully!".equals(message)) {
                return ResponseEntity.ok(message);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if ("User not found".equals(errorMessage) || "Incorrect password".equals(errorMessage) || "Authentication failed".equals(errorMessage)) {
                status = HttpStatus.UNAUTHORIZED;
            }
            return ResponseEntity.status(status).body(errorMessage);
        }
    }


}
