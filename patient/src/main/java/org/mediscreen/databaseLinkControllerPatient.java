package org.mediscreen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class databaseLinkControllerPatient {
    @Autowired
    private PatientRepository patientRepository;
    @GetMapping("database/link")
    public List<Patient> checkAllPatients() {
        return patientRepository.findAll();
    }
    @GetMapping("database/link/id")
    public Patient databaseLinkById(@RequestParam int id){
        return patientRepository.findById(id).orElse(null);
    }
    @GetMapping("database/link/family")
    public Patient databaseLinkByFamilyName(@RequestParam String familyName){
        return patientRepository.findByFamilyName(familyName).orElse(null);
    }
    @PostMapping("patient/add")
    public ResponseEntity<String> addPatient(@RequestParam String family,
                                             @RequestParam String given,
                                             @RequestParam String dob,  // Keep it as String
                                             @RequestParam String sex,
                                             @RequestParam String address,
                                             @RequestParam String phone) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Define the date format

        try {
            Date date = Date.from(LocalDate.parse(dob, dateFormatter).atStartOfDay(ZoneId.systemDefault()).toInstant()); // Parse the String to LocalDate
            // Create a new Patient object
            Patient newPatient = new Patient();
            newPatient.setFamilyName(family);
            newPatient.setGivenName(given);
            newPatient.setDob(date); // Ensure your Patient class supports LocalDate
            newPatient.setSex(sex);
            newPatient.setAddress(address);
            newPatient.setPhoneNumber(phone);

            // Call the service to save the patient
            patientRepository.save(newPatient);

            return ResponseEntity.status(HttpStatus.CREATED).body("Patient added successfully");

        } catch (DateTimeParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding patient: " + e.getMessage());
        }
    }
}
