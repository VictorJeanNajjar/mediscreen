package org.assessments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("assessment/")
public class AssessmentsController {
    @Autowired
    private AssessmentsService assessmentsService;
    @GetMapping("patientList")
    public String patientList(Model model){
        return assessmentsService.checkAllAssessments(model);
    }
    @GetMapping("riskcalCulation/{patientId}")
    public String riskCalculation(@PathVariable Integer patientId, Model model){
        return assessmentsService.calculateRisk(patientId, model);
    }
}
