package org.docnotes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/docNotes")  // Base path for all endpoints related to doctor notes
public class DocNotesController {

    @Autowired
    private DocNotesService docNotesService;

    // Display all doctor notes for a specific patient
    @GetMapping("/patient/{patientId}")
    public String showPatientNotes(@PathVariable int patientId, Model model) {
        return docNotesService.showPatientDocNotes(patientId, model);
    }

    // Add or update doctor notes for a specific patient
    @PostMapping("/add/{patientId}")
    public String addDocNotes(@PathVariable int patientId,
                                 @RequestParam String notes,
                                 Model model) {
        // This method will add the notes or update if they already exist
        return docNotesService.addDocNoteService(patientId, model, notes);
    }
    @PostMapping("/update/{patientId}")
    public String updateDoctorNote(@PathVariable int patientId, @RequestParam String oldNote, @RequestParam String newNote, Model model) {
        return docNotesService.updateDoctorNote(patientId, oldNote, newNote, model);

    }
    // Remove a specific doctor note for a specific patient
    @PostMapping("/remove/{patientId}")
    public String removeDoctorNote(@PathVariable int patientId,
                                   @RequestParam String note,
                                   Model model) {
        String result = docNotesService.removeDoctorNote(patientId, note, model);
        // Redirect to show updated notes after removal
        return "redirect:/docNotes/patient/" + patientId;  // Redirect to patient notes view
    }
    // Show all doctor notes (optional)
    @GetMapping("/patientList")
    public String listAllDocNotes(Model model) {
        docNotesService.updatePatients();
        return docNotesService.checkAllDocNotes(model);
    }
}