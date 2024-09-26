package org.assessments;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AssessmentsService {
    @Autowired
    private RestTemplate restTemplate;
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
                patients = objectMapper.readValue(jsonResponse, new TypeReference<Patient>() {
                });
            } else {
                System.err.println("Received null response from the endpoint");
            }
        } catch (Exception e) {
            System.err.println("Error fetching patients from endpoint: " + e.getMessage());
            e.printStackTrace();
        }

        return patients;
    }
    public Patient fetchSinglePatientsFromEndpointWithFamilyName(String family) {
        String url = "http://springboot-patient-app:8081/database/link/family?familyName=" + family;
        Patient patients = null;

        try {
            // Fetch the JSON response from the API
            String jsonResponse = restTemplate.getForObject(url, String.class);

            if (jsonResponse != null) {
                // Parse JSON response to List<Patient> using Jackson
                ObjectMapper objectMapper = new ObjectMapper();
                patients = objectMapper.readValue(jsonResponse, new TypeReference<Patient>() {
                });
            } else {
                System.err.println("Received null response from the endpoint");
            }
        } catch (Exception e) {
            System.err.println("Error fetching patients from endpoint: " + e.getMessage());
            e.printStackTrace();
        }

        return patients;
    }
    public DocNotes fetchSingleDocNotesFromEndpoint(Integer id) {
        String url = "http://springboot-docnotes-app:8082/link/docNotes/id?id=" + id;
        DocNotes docNotes = null;

        try {
            // Fetch the JSON response from the API
            String jsonResponse = restTemplate.getForObject(url, String.class);

            if (jsonResponse != null) {
                // Parse JSON response to List<Patient> using Jackson
                ObjectMapper objectMapper = new ObjectMapper();
                docNotes = objectMapper.readValue(jsonResponse, new TypeReference<DocNotes>() {});
            } else {
                System.err.println("Received null response from the endpoint");
            }
        } catch (Exception e) {
            System.err.println("Error fetching patients from endpoint: " + e.getMessage());
            e.printStackTrace();
        }

        return docNotes;
    }
    public int ageCalculator(Date date){
        LocalDate birthLocalDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate currentDate = LocalDate.now();
        return (int) ChronoUnit.YEARS.between(birthLocalDate, currentDate);
    }
    public String checkAllAssessments(Model model) {
        List<Patient> patient = fetchPatientsFromEndpoint();
        model.addAttribute("patients", patient);  // Change to "patients"
        return "assessments/list";
    }
    public String calculateRisk(Integer id, Model model) {
        Patient patient = fetchSinglePatientsFromEndpoint(id);
        DocNotes docNotes = fetchSingleDocNotesFromEndpoint(id);
        Set<String> foundTriggerWords = new HashSet<>();

        if (patient == null) {
            String message = "User doesn't exist";
            model.addAttribute("message", message);
            return "assessments/check";
        }
        int age = ageCalculator(patient.getDob()); // Calculate age once
        int triggerNumber = 0;
        int ageOfConcern = 30;
        TriggerWords triggerWords = new TriggerWords();
        String danger = "TestNone";
        String message = patient.getGivenName() + " " + patient.getFamilyName() + " (age " + age + ") diabetes assessment is: None";
        System.out.println(docNotes);
        // If docNotes is null, return early
        if (docNotes == null) {
            model.addAttribute("message", message);
            return "assessments/check";
        }

        // Count trigger words in the notes
        for (String word : triggerWords.getWords()) {
            // Iterate through each note
            for (String note : docNotes.getNotes()) {
                // Check if the note contains the trigger word (case-insensitive)
                if (note.toLowerCase().contains(word.toLowerCase())) {
                    foundTriggerWords.add(word.toLowerCase()); // Add the trigger word in lowercase
                    break; // Break after finding the first occurrence
                }
            }
        }

        // Return the number of unique trigger words found
        triggerNumber =  foundTriggerWords.size();

        // Assess risk based on age, gender, and trigger number
        if (age >= ageOfConcern && triggerNumber >= 2 && triggerNumber < 6) {
            message = patient.getGivenName() + " " + patient.getFamilyName() + " (age " + age + ") diabetes assessment is: Borderline";
            danger = "TestBorderline";
        } else if ((age < ageOfConcern && triggerNumber >= 3 && triggerNumber < 5 && Objects.equals(patient.getSex(), "M")) ||
                (age < ageOfConcern && triggerNumber >= 4 && triggerNumber < 7 && Objects.equals(patient.getSex(), "F")) ||
                (age >= ageOfConcern && triggerNumber >= 6 && triggerNumber < 8)) {
            message = patient.getGivenName() + " " + patient.getFamilyName() + " (age " + age + ") diabetes assessment is: In Danger";
            danger = "TestInDanger";
        } else if ((age < ageOfConcern && triggerNumber >= 5 && Objects.equals(patient.getSex(), "M")) ||
                (age < ageOfConcern && triggerNumber >= 7 && Objects.equals(patient.getSex(), "F")) ||
                (age >= ageOfConcern && triggerNumber >= 8)) {
            message = patient.getGivenName() + " " + patient.getFamilyName() + " (age " + age + ") diabetes assessment is: Early Onset";
            danger = "TestEarlyOnset";
        }

        // Add the message to the model
        model.addAttribute("message", message);
        return "assessments/check";
    }
}
