package fr.norsys.upload_doc.exception;

public class AccesAlreadyExistException extends Exception {
    public AccesAlreadyExistException() {
        super("This access is already given to this user for the document");
    }
}
