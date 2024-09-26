package org.assessments;

import java.util.List;


public class TriggerWords {
    public List<String> words = List.of(
            "Hemoglobin A1C",
            "Microalbumin",
            "Body Height",
            "Body Weight",
            "Smoker",
            "Abnormal",
            "Cholesterol",
            "Dizziness",
            "Relapse",
            "Reaction",
            "Antibodies"
    );
    public TriggerWords() {
    }
    public List<String> getWords() {
        return words;
    }
}
