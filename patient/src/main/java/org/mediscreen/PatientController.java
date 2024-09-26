package org.mediscreen;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;
    @RequestMapping("/list")
    public String allPatientsList(Model model)
    {
        return patientService.checkAllPatients(model);
    }

    @GetMapping("/new/add")
    public String addPatientForm(Patient patient) {
        return "patient/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid Patient patient, BindingResult result, Model model) {
        return patientService.validateService(patient, result, model);
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        return patientService.showUpdateFormService(id, model);
    }

    @PostMapping("/update/{id}")
    public String updatePatient(@PathVariable("id") Integer id, @Valid Patient patient,
                            BindingResult result, Model model) {
        return patientService.updatePatientService(id, patient, result, model);
    }
    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable("id") Integer id, Model model) {
        return patientService.deletePatientService(id, model);
    }
}