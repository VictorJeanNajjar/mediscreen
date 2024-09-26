import org.assessments.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class DatabaseLinkTest {

    @InjectMocks
    private DatabaseLink databaseLink;

    @Mock
    private AssessmentsService assessmentsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateRisk_UserDoesNotExistId() {
        // Arrange
        when(assessmentsService.fetchSinglePatientsFromEndpoint(anyInt())).thenReturn(null);

        // Act
        String result = databaseLink.calculateRiskId(1);

        // Assert
        assertEquals("User doesn't exist", result);
    }

    @Test
    void testCalculateRisk_NoDocNotesId() {
        // Arrange
        Patient patient = new Patient(1, "John", "Doe", new Date(80, Calendar.FEBRUARY, 1), "M");
        when(assessmentsService.fetchSinglePatientsFromEndpoint(1)).thenReturn(patient);
        when(assessmentsService.fetchSingleDocNotesFromEndpoint(1)).thenReturn(null);

        // Act
        String result = databaseLink.calculateRiskId(1);

        // Assert
        assertEquals("Patient: TestNone (age0) diabetes assessment is: None", result);
    }

    @Test
    void testCalculateRisk_BorderlineRiskId() {
        // Arrange
        Patient patient = new Patient(1, "John", "Doe", new Date(80, 0, 1), "M");
        DocNotes docNotes = new DocNotes();
        ArrayList<String> notes = new ArrayList<>();
        notes.add("Patient shows signs of diabetes. Abnormal Cholesterol Antibodies");
        docNotes.setNotes(notes);

        when(assessmentsService.fetchSinglePatientsFromEndpoint(1)).thenReturn(patient);
        when(assessmentsService.fetchSingleDocNotesFromEndpoint(1)).thenReturn(docNotes);
        when(assessmentsService.ageCalculator(patient.getDob())).thenReturn(30);

        // Act
        String result = databaseLink.calculateRiskId(1);

        // Assert
        assertEquals("Patient: TestBorderline (age30) diabetes assessment is: Borderline", result);
    }

    @Test
    void testCalculateRisk_InDangerRiskId() {
        // Arrange
        Patient patient = new Patient(1, "John", "Doe", new Date(1970, Calendar.JANUARY, 1), "M"); // Age < 30
        DocNotes docNotes = new DocNotes();
        ArrayList<String> notes = new ArrayList<>();
        notes.add("Patient shows signs of diabetes. Smoker. Abnormal Cholesterol Antibodies");;
        docNotes.setNotes(notes);

        when(assessmentsService.fetchSinglePatientsFromEndpoint(1)).thenReturn(patient);
        when(assessmentsService.fetchSingleDocNotesFromEndpoint(1)).thenReturn(docNotes);
        when(assessmentsService.ageCalculator(patient.getDob())).thenReturn(25);

        // Act
        String result = databaseLink.calculateRiskId(1);

        // Assert
        assertEquals("Patient: TestInDanger (age25) diabetes assessment is: In Danger", result);
    }

    @Test
    void testCalculateRisk_EarlyOnsetRiskId() {
        // Arrange
        Patient patient = new Patient(1, "Jane", "Doe", new Date(90, Calendar.FEBRUARY, 1), "F"); // Age < 30
        DocNotes docNotes = new DocNotes();
        ArrayList<String> notes = new ArrayList<>();
        notes.add("Patient shows signs of diabetes. Smoker. Abnormal Cholesterol Antibodies Body Weight Body Height Microalbumin");
        docNotes.setNotes(notes);

        when(assessmentsService.fetchSinglePatientsFromEndpoint(1)).thenReturn(patient);
        when(assessmentsService.fetchSingleDocNotesFromEndpoint(1)).thenReturn(docNotes);
        when(assessmentsService.ageCalculator(patient.getDob())).thenReturn(25);

        // Mocking trigger words logic
        TriggerWords triggerWords = new TriggerWords();

        // Act
        String result = databaseLink.calculateRiskId(1);

        // Assert
        assertEquals("Patient: TestEarlyOnset (age25) diabetes assessment is: Early Onset", result);
    }
    @Test
    void testCalculateRisk_UserDoesNotExistFamily() {
        // Arrange
        when(assessmentsService.fetchSinglePatientsFromEndpoint(anyInt())).thenReturn(null);

        // Act
        String result = databaseLink.calculateRiskFamily("Doe");

        // Assert
        assertEquals("User doesn't exist", result);
    }

    @Test
    void testCalculateRisk_NoDocNotesFamily() {
        // Arrange
        Patient patient = new Patient(1, "John", "Doe", new Date(80, Calendar.FEBRUARY, 1), "M");
        when(assessmentsService.fetchSinglePatientsFromEndpointWithFamilyName("Doe")).thenReturn(patient);
        when(assessmentsService.fetchSingleDocNotesFromEndpoint(1)).thenReturn(null);

        // Act
        String result = databaseLink.calculateRiskFamily("Doe");

        // Assert
        assertEquals("Patient: TestNone (age0) diabetes assessment is: None", result);
    }

    @Test
    void testCalculateRisk_BorderlineRiskFamily() {
        // Arrange
        Patient patient = new Patient(1, "John", "Doe", new Date(80, 0, 1), "M");
        DocNotes docNotes = new DocNotes();
        ArrayList<String> notes = new ArrayList<>();
        notes.add("Patient shows signs of diabetes. Abnormal Cholesterol Antibodies");
        docNotes.setNotes(notes);

        when(assessmentsService.fetchSinglePatientsFromEndpointWithFamilyName("Doe")).thenReturn(patient);
        when(assessmentsService.fetchSingleDocNotesFromEndpoint(1)).thenReturn(docNotes);
        when(assessmentsService.ageCalculator(patient.getDob())).thenReturn(30);

        // Act
        String result = databaseLink.calculateRiskFamily("Doe");

        // Assert
        assertEquals("Patient: TestBorderline (age30) diabetes assessment is: Borderline", result);
    }

    @Test
    void testCalculateRisk_InDangerRiskFamily() {
        // Arrange
        Patient patient = new Patient(1, "John", "Doe", new Date(1970, Calendar.JANUARY, 1), "M"); // Age < 30
        DocNotes docNotes = new DocNotes();
        ArrayList<String> notes = new ArrayList<>();
        notes.add("Patient shows signs of diabetes. Smoker. Abnormal Cholesterol Antibodies");;
        docNotes.setNotes(notes);

        when(assessmentsService.fetchSinglePatientsFromEndpointWithFamilyName("Doe")).thenReturn(patient);
        when(assessmentsService.fetchSingleDocNotesFromEndpoint(1)).thenReturn(docNotes);
        when(assessmentsService.ageCalculator(patient.getDob())).thenReturn(25);

        // Act
        String result = databaseLink.calculateRiskFamily("Doe");

        // Assert
        assertEquals("Patient: TestInDanger (age25) diabetes assessment is: In Danger", result);
    }

    @Test
    void testCalculateRisk_EarlyOnsetRiskFamily() {
        // Arrange
        Patient patient = new Patient(1, "Jane", "Doe", new Date(90, Calendar.FEBRUARY, 1), "F"); // Age < 30
        DocNotes docNotes = new DocNotes();
        ArrayList<String> notes = new ArrayList<>();
        notes.add("Patient shows signs of diabetes. Smoker. Abnormal Cholesterol Antibodies Body Weight Body Height Microalbumin");
        docNotes.setNotes(notes);

        when(assessmentsService.fetchSinglePatientsFromEndpointWithFamilyName("Doe")).thenReturn(patient);
        when(assessmentsService.fetchSingleDocNotesFromEndpoint(1)).thenReturn(docNotes);
        when(assessmentsService.ageCalculator(patient.getDob())).thenReturn(25);

        // Mocking trigger words logic
        TriggerWords triggerWords = new TriggerWords();

        // Act
        String result = databaseLink.calculateRiskFamily("Doe");

        // Assert
        assertEquals("Patient: TestEarlyOnset (age25) diabetes assessment is: Early Onset", result);
    }
}
