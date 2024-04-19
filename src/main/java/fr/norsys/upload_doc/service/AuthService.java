package fr.norsys.upload_doc.service;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import fr.norsys.upload_doc.dto.SignInRequest;
import fr.norsys.upload_doc.dto.SignUpRequest;

public interface AuthService {

    String signUpUser(SignUpRequest signUpRequest) throws FirebaseAuthException;
    String signInUser(SignInRequest signInRequest);
}
