package fr.norsys.upload_doc.exception;

public class DocumentNotFound extends Exception{
    public DocumentNotFound(String key) {
        super("No document associated  with the id  '" + key);
    }
}
