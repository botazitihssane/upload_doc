package fr.norsys.upload_doc.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String key) {
        super("No user exist with the email '" + key);
    }
}
