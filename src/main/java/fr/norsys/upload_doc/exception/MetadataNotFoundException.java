package fr.norsys.upload_doc.exception;

public class MetadataNotFoundException extends RuntimeException {
    public MetadataNotFoundException(String key) {
        super("The metadata key '" + key + "' does not exist for the document.");
    }
}
