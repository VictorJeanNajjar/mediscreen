package org.docnotes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocNotesService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DocNotesRepository docNotesRepository;

    public List<Patient> fetchPatientsFromEndpoint() {
        String url = "http://springboot-patient-app:8081/database/link";
        List<Patient> patients = new ArrayList<>();

        try {
            // Fetch the JSON response from the API
            String jsonResponse = restTemplate.getForObject(url, String.class);

            if (jsonResponse != null) {
                // Parse JSON response to List<Patient> using Jackson
                ObjectMapper objectMapper = new ObjectMapper();
                patients = objectMapper.readValue(jsonResponse, new TypeReference<List<Patient>>() {});
            } else {
                System.err.println("Received null response from the endpoint");
            }
        } catch (Exception e) {
            System.err.println("Error fetching patients from endpoint: " + e.getMessage());
            e.printStackTrace();
        }

        return patients;
    }
    public Patient fetchSinglePatientsFromEndpoint(Integer id) {
        String url = "http://springboot-patient-app:8081/database/link/id?id=" + id;
        Patient patients = null;

        try {
            // Fetch the JSON response from the API
            String jsonResponse = restTemplate.getForObject(url, String.class);

            if (jsonResponse != null) {
                // Parse JSON response to List<Patient> using Jackson
                ObjectMapper objectMapper = new ObjectMapper();
                patients = objectMapper.readValue(jsonResponse, new TypeReference<Patient>() {});
            } else {
                System.err.println("Received null response from the endpoint");
            }
        } catch (Exception e) {
            System.err.println("Error fetching patients from endpoint: " + e.getMessage());
            e.printStackTrace();
        }

        return patients;
    }
    public String checkAllDocNotes(Model model) {
        List<Patient> patient = fetchPatientsFromEndpoint();
        model.addAttribute("patients", patient);  // Change to "patients"
        return "DocNotes/list";
    }
    public String addDocNoteService(Integer id, Model model, String notes) {
        Optional<DocNotes> optionalDocNotes = docNotesRepository.findByPatientId(id);
        DocNotes docNotes;
        if (optionalDocNotes.isPresent()) {
            // If DocNotes exist, use it
            docNotes = optionalDocNotes.get();
            ArrayList<String> allDoctorNotes = docNotes.getNotes();
            allDoctorNotes.add(notes);
            docNotes.setNotes(allDoctorNotes);
            docNotesRepository.save(docNotes);
        } else {
            // If DocNotes does not exist, create a new one for this patient
            docNotes = new DocNotes();
            ArrayList<String> allDoctorNotes = new ArrayList<>();
            docNotes.setPatientId(id);  // Associate with patient ID
            allDoctorNotes.add(notes);
            docNotes.setNotes(allDoctorNotes);
            docNotesRepository.save(docNotes); // Save the new DocNotes entry to the database
        }

        // Add DocNotes to the model and return the update form view
        model.addAttribute("DocNotes", docNotes);
        return "redirect:/docNotes/patient/" + id;  // Return the form for updating the DocNotes
    }
    public String showPatientDocNotes(int patientId, Model model){
        Optional<DocNotes> currentPatient = docNotesRepository.findByPatientId(patientId);

        if (currentPatient.isPresent()){
            Patient patient = fetchSinglePatientsFromEndpoint(patientId);
            String patientFullName = patient.getGivenName() + " " + patient.getFamilyName();
            List<String> currentPatientDocNotes = currentPatient.get().getNotes();

            model.addAttribute("currentPatientDocNotes", currentPatientDocNotes);
            model.addAttribute("patientFullName", patientFullName);
        }else {
            model.addAttribute("currentPatientDocNotes", new ArrayList<>()); // Provide an empty list if not found
            model.addAttribute("patientFullName", ""); // Set empty full name if no notes
        }

        return "DocNotes/patientNotes";
    }

    public String removeDoctorNote(int patientId, String note, Model model) {
        Optional<DocNotes> currentPatientOp = docNotesRepository.findByPatientId(patientId);
        if (currentPatientOp.isPresent()) {
            DocNotes currentPatient = currentPatientOp.get();
            ArrayList<String> newNotes = currentPatient.getNotes();
            newNotes.remove(note);
            currentPatient.setNotes(newNotes);
            docNotesRepository.save(currentPatient); // Save the updated notes
            model.addAttribute("DocNotes", currentPatient); // Pass the updated notes to the model
        } else {
            model.addAttribute("errorMessage", "No doctor notes found for the specified patient.");
        }
        return "redirect:/docNotes/patient/" + patientId; // Ensure the view is able to access the updated model
    }
    public String updateDoctorNote(int patientId, String oldNote, String newNote, Model model) {
        Optional<DocNotes> currentPatientOp = docNotesRepository.findByPatientId(patientId);

        if (currentPatientOp.isPresent()) {
            DocNotes currentPatient = currentPatientOp.get();
            ArrayList<String> notes = currentPatient.getNotes();
            if (notes.contains(oldNote)) {
                int index = notes.indexOf(oldNote);
                notes.set(index, newNote);
                currentPatient.setNotes(notes);
                docNotesRepository.save(currentPatient);
                model.addAttribute("DocNotes", currentPatient);
            } else {
                model.addAttribute("errorMessage", "The specified note to update was not found.");
            }
        } else {
            model.addAttribute("errorMessage", "No doctor notes found for the specified patient.");
        }

        return "redirect:/docNotes/patient/" + patientId; // Redirect to the patient's notes page
    }

    public void updatePatients() {
        List<DocNotes> allDocNotes = docNotesRepository.findAll();

        // Log the size of both lists for debugging
        System.out.println("Total DocNotes fetched: " + allDocNotes.size());
        // Iterate through all document notes and check if the patient exists
        for (DocNotes docNotes : allDocNotes) {
            Integer docPatientId = docNotes.getPatientId();
            System.out.println("Checking DocNotes for patient ID: " + docPatientId);

            if (fetchSinglePatientsFromEndpoint(docPatientId) == null) {
                deleteDocNotesService(docNotes.getDocNotesId());
            }
        }
    }
    public void deleteDocNotesService(ObjectId id) {
        docNotesRepository.deleteById(id);
    }
}
