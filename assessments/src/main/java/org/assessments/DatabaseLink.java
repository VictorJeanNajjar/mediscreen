package org.assessments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@RestController
public class DatabaseLink {
    @Autowired
    private AssessmentsService assessmentsService;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("assess/id")
    public String calculateRiskId(@RequestParam("patId")Integer id) {
        Patient patient = assessmentsService.fetchSinglePatientsFromEndpoint(id);
        DocNotes docNotes = assessmentsService.fetchSingleDocNotesFromEndpoint(id);
        Set<String> foundTriggerWords = new HashSet<>();

        if (patient == null) {
            return "User doesn't exist";
        }

        int age = assessmentsService.ageCalculator(patient.getDob()); // Calculate age once
        int triggerNumber = 0;
        int ageOfConcern = 30;
        TriggerWords triggerWords = new TriggerWords();
        String  message = "Patient: " + "TestNone" + " (age" + assessmentsService.ageCalculator(patient.getDob()) + ") diabetes assessment is: None";

        // If docNotes is null, return early
        if (docNotes == null) {
            return message;
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
            message = "Patient: " + "TestBorderline" + " (age" + assessmentsService.ageCalculator(patient.getDob()) + ") diabetes assessment is: Borderline";
        }else if ((age < ageOfConcern && triggerNumber >= 3 && triggerNumber < 5 && Objects.equals(patient.getSex(), "M")) ||
                (age < ageOfConcern && triggerNumber >= 4 && triggerNumber < 7 && Objects.equals(patient.getSex(), "F")) ||
                (age >= ageOfConcern && triggerNumber >= 6 && triggerNumber < 8)) {
            message = "Patient: " + "TestInDanger" + " (age" + assessmentsService.ageCalculator(patient.getDob()) + ") diabetes assessment is: In Danger";
        }else if ((age < ageOfConcern && triggerNumber >= 5 && Objects.equals(patient.getSex(), "M")) ||
                (age < ageOfConcern && triggerNumber >= 7 && Objects.equals(patient.getSex(), "F")) ||
                (age >= ageOfConcern && triggerNumber >= 8)) {
            message = "Patient: " + "TestEarlyOnset" + " (age" + assessmentsService.ageCalculator(patient.getDob()) + ") diabetes assessment is: Early Onset";
        }
        System.out.println(docNotes.toString());
        // Add the message to the model
        return message;
    }
    @PostMapping("assess/familyName")
    public String calculateRiskFamily(@RequestParam("familyName")String family) {
        Patient patient = assessmentsService.fetchSinglePatientsFromEndpointWithFamilyName(family);
        if (patient == null) {
            return "User doesn't exist";
        }
        Integer id = patient.getPatientId();
        DocNotes docNotes = assessmentsService.fetchSingleDocNotesFromEndpoint(id);
        Set<String> foundTriggerWords = new HashSet<>();
        int age = assessmentsService.ageCalculator(patient.getDob()); // Calculate age once
        int triggerNumber = 0;
        int ageOfConcern = 30;
        TriggerWords triggerWords = new TriggerWords();
        String  message = "Patient: " + "TestNone" + " (age" + assessmentsService.ageCalculator(patient.getDob()) + ") diabetes assessment is: None";

        // If docNotes is null, return early
        if (docNotes == null) {
            return message;
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
            message = "Patient: " + "TestBorderline" + " (age" + assessmentsService.ageCalculator(patient.getDob()) + ") diabetes assessment is: Borderline";
        }else if ((age < ageOfConcern && triggerNumber >= 3 && triggerNumber < 5 && Objects.equals(patient.getSex(), "M")) ||
                (age < ageOfConcern && triggerNumber >= 4 && triggerNumber < 7 && Objects.equals(patient.getSex(), "F")) ||
                (age >= ageOfConcern && triggerNumber >= 6 && triggerNumber < 8)) {
            message = "Patient: " + "TestInDanger" + " (age" + assessmentsService.ageCalculator(patient.getDob()) + ") diabetes assessment is: In Danger";
        }else if ((age < ageOfConcern && triggerNumber >= 5 && Objects.equals(patient.getSex(), "M")) ||
                (age < ageOfConcern && triggerNumber >= 7 && Objects.equals(patient.getSex(), "F")) ||
                (age >= ageOfConcern && triggerNumber >= 8)) {
            message = "Patient: " + "TestEarlyOnset" + " (age" + assessmentsService.ageCalculator(patient.getDob()) + ") diabetes assessment is: Early Onset";
        }
        System.out.println(docNotes.toString());
        // Add the message to the model
        return message;
    }
}
