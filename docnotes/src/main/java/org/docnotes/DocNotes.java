package org.docnotes;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotEmpty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;


@Document
public class DocNotes {
    @JsonSerialize(using = ObjectIdSerializer.class)
    @Id
    public ObjectId docNotesId;
    @NotEmpty
    public Integer patientId;
    private ArrayList<String> notes;

    public DocNotes() {
    }

    public DocNotes(ObjectId docNotesId, Integer patientId, ArrayList<String> notes) {
        this.docNotesId = docNotesId;
        this.patientId = patientId;
        this.notes = notes;
    }

    public ObjectId getDocNotesId() {
        return docNotesId;
    }

    public void setDocNotesId(ObjectId docNotesId) {
        this.docNotesId = docNotesId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }
}
