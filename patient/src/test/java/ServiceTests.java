
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mediscreen.Patient;
import org.mediscreen.PatientRepository;
import org.mediscreen.PatientService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ServiceTests {

    @InjectMocks
    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkAllPatients_ShouldReturnPatientListView() {
        // Arrange
        when(patientRepository.findAll()).thenReturn(Arrays.asList(new Patient(), new Patient()));

        // Act
        String viewName = patientService.checkAllPatients(model);

        // Assert
        verify(model).addAttribute(eq("patient"), anyList());
        assertEquals("patient/list", viewName);
    }

    @Test
    void validateService_ShouldRedirectToList_WhenNoErrors() {
        // Arrange
        Patient patient = new Patient();
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String viewName = patientService.validateService(patient, bindingResult, model);

        // Assert
        verify(patientRepository).save(patient);
        assertEquals("redirect:/patient/list", viewName);
    }

    @Test
    void validateService_ShouldReturnAddView_WhenErrorsExist() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String viewName = patientService.validateService(new Patient(), bindingResult, model);

        // Assert
        assertEquals("patient/add", viewName);
    }

    @Test
    void showUpdateFormService_ShouldReturnUpdateView_WhenPatientFound() {
        // Arrange
        Integer patientId = 1;
        Patient patient = new Patient();
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        // Act
        String viewName = patientService.showUpdateFormService(patientId, model);

        // Assert
        verify(model).addAttribute("patient", patient);
        assertEquals("patient/update", viewName);
    }

    @Test
    void showUpdateFormService_ShouldReturnError_WhenPatientNotFound() {
        // Arrange
        Integer patientId = 1;
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // Act
        String viewName = patientService.showUpdateFormService(patientId, model);

        // Assert
        verify(model).addAttribute("error", "Patient not found");
        assertEquals("error", viewName);
    }

    @Test
    void updatePatientService_ShouldRedirectToList_WhenNoErrorsAndIdMatches() {
        // Arrange
        Integer patientId = 1;
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // Act
        String viewName = patientService.updatePatientService(patientId, patient, bindingResult, model);

        // Assert
        assertEquals("redirect:/patient/list", viewName);
    }

    @Test
    void updatePatientService_ShouldReturnUpdateView_WhenErrorsExist() {
        // Arrange
        Integer patientId = 1;
        Patient patient = new Patient();
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String viewName = patientService.updatePatientService(patientId, patient, bindingResult, model);

        // Assert
        assertEquals("patient/update", viewName);
    }

    @Test
    void updatePatientService_ShouldReturnError_WhenIdMismatch() {
        // Arrange
        Integer patientId = 1;
        Patient patient = new Patient();
        patient.setPatientId(2);  // Different ID

        // Act
        String viewName = patientService.updatePatientService(patientId, patient, bindingResult, model);

        // Assert
        verify(model).addAttribute("error", "ID mismatch. Please try again.");
        assertEquals("error", viewName);
    }

    @Test
    void deletePatientService_ShouldRedirectToList() {
        // Arrange
        Integer patientId = 1;

        // Act
        String viewName = patientService.deletePatientService(patientId, model);

        // Assert
        verify(patientRepository).deleteById(patientId);
        assertEquals("redirect:/patient/list", viewName);
    }
}
