package fr.norsys.upload_doc.entity;

import fr.norsys.upload_doc.enumeration.Droit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccesDroitsId implements Serializable {
    private UUID acces_id;
    private Droit droits;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccesDroitsId that = (AccesDroitsId) o;
        return Objects.equals(acces_id, that.acces_id) && droits == that.droits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(acces_id, droits);
    }
}
