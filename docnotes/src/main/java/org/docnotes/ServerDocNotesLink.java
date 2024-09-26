package org.docnotes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ServerDocNotesLink {
    @Autowired
    private DocNotesRepository docNotesRepository;
    @GetMapping("/link/docNotes")
    public List<DocNotes> showAllDocNotes(){
        return docNotesRepository.findAll();
    }
    @GetMapping("/link/docNotes/id")
    public DocNotes databaseLinkById(@RequestParam Integer id){
        return docNotesRepository.findByPatientId(id).orElse(null);
    }
    @GetMapping("/patHistory/add")
    public void addDocNoteServiceLink(@RequestParam Integer patId, @RequestParam String note) {
        Optional<DocNotes> optionalDocNotes = docNotesRepository.findByPatientId(patId);
        DocNotes docNotes;
        if (optionalDocNotes.isPresent()) {
            // If DocNotes exist, use it
            docNotes = optionalDocNotes.get();
            ArrayList<String> allDoctorNotes = docNotes.getNotes();
            allDoctorNotes.add(note);
            docNotes.setNotes(allDoctorNotes);
            docNotesRepository.save(docNotes);
        } else {
            // If DocNotes does not exist, create a new one for this patient
            docNotes = new DocNotes();
            ArrayList<String> allDoctorNotes = new ArrayList<>();
            docNotes.setPatientId(patId);  // Associate with patient ID
            allDoctorNotes.add(note);
            docNotes.setNotes(allDoctorNotes);
            docNotesRepository.save(docNotes); // Save the new DocNotes entry to the database
        }
    }
}
