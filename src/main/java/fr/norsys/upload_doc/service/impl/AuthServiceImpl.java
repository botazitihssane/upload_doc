package fr.norsys.upload_doc.service.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import fr.norsys.upload_doc.dto.SignInRequest;
import fr.norsys.upload_doc.dto.SignUpRequest;
import fr.norsys.upload_doc.entity.Utilisateur;
import fr.norsys.upload_doc.exception.UserNotFoundException;
import fr.norsys.upload_doc.repository.UtilisateurRepository;
import fr.norsys.upload_doc.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class AuthServiceImpl implements AuthService {
  @Autowired
    public UtilisateurRepository utilisateurRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    public FirebaseAuth firebaseAuth;

    public String signUpUser(SignUpRequest signUpRequest) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(signUpRequest.getEmail())
                .setPassword(signUpRequest.getPassword());
        String hashedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        Utilisateur user=new Utilisateur();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(hashedPassword);
        utilisateurRepository.save(user);
        return "User registered successfully!";
    }
    @Override
    public String signInUser(SignInRequest signInRequest) {
        try {

            Utilisateur user = utilisateurRepository.findByEmail(signInRequest.getEmail());
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }


            if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
                throw new Exception("Incorrect password");
            }


            UserRecord userRecord = firebaseAuth.getUserByEmail(signInRequest.getEmail());


            return "User authenticated successfully!";
        } catch (UserNotFoundException e) {
            return e.getMessage();
        } catch (Exception e) {
            return e.getMessage();
        }
    }




}
