package org.assessments;

import org.bson.types.ObjectId;

import java.util.ArrayList;


public class DocNotes {
    public ObjectId docNotesId;
    public Integer patientId;
    private ArrayList<String> notes;

    @Override
    public String toString() {
        return "DocNotes{" +
                "docNotesId=" + docNotesId +
                ", patientId=" + patientId +
                ", notes=" + notes +
                '}';
    }

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
