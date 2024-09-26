package org.mediscreen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PatientService {
    @Autowired
    public PatientRepository patientRepository;

    public String checkAllPatients(Model model) {
        List<Patient> patient = patientRepository.findAll();
        model.addAttribute("patient", patient);  // Change to "patients"
        return "patient/list";
    }
    public String validateService(Patient patient, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "patient/add";
        }
        patientRepository.save(patient);
        return "redirect:/patient/list";
    }

    public String showUpdateFormService(Integer id, Model model) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isPresent()) {
            model.addAttribute("patient", optionalPatient.get());
            return "patient/update";
        } else {
            // Handle the case where the patient is not found
            model.addAttribute("error", "Patient not found");
            return "error";
        }
    }

    public String updatePatientService(Integer id, Patient patient,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("patient", patient);
            return "patient/update";
        }if (!Objects.equals(id, patient.getPatientId())) {
            model.addAttribute("error", "ID mismatch. Please try again.");
            return "error";
        }
        patientRepository.save(patient);
        return "redirect:/patient/list";
    }
    public String deletePatientService(Integer id, Model model) {
        patientRepository.deleteById(id);
        return "redirect:/patient/list";
    }
}
