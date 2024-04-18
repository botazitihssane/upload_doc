package fr.norsys.upload_doc.exception;

public class AccesNotFoundException extends Exception {
    public AccesNotFoundException() {
        super("The user does not have this access for the document to be revoked");
    }
}
