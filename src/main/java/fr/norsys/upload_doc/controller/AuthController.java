package fr.norsys.upload_doc.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import fr.norsys.upload_doc.dto.SignInRequest;
import fr.norsys.upload_doc.dto.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private FirebaseAuth firebaseAuth;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest) {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(signUpRequest.getEmail())
                    .setPassword(signUpRequest.getPassword());
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            return ResponseEntity.ok("User registered successfully!");
        } catch (FirebaseAuthException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

//    @PostMapping("/signin")
//    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest) {
////        try {
////            FirebaseAuth.getInstance().signInWithEmailAndPassword(
////                    signInRequest.getEmail(), signInRequest.getPassword());
////            return ResponseEntity.ok("User signed in successfully!");
////        } catch (FirebaseAuthException e) {
////            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
////        }
//    }
}
