import org.bson.types.ObjectId;
import org.docnotes.DocNotes;
import org.docnotes.DocNotesRepository;
import org.docnotes.DocNotesService;
import org.docnotes.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class DocNotesServiceTest {

    @InjectMocks
    private DocNotesService docNotesService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private DocNotesRepository docNotesRepository;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchPatientsFromEndpoint_ShouldReturnListOfPatients() throws Exception {
        // Arrange
        String jsonResponse = "[{\"patientId\":1,\"givenName\":\"John\",\"familyName\":\"Doe\"}]";
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenReturn(jsonResponse);

        // Act
        List<Patient> patients = docNotesService.fetchPatientsFromEndpoint();

        // Assert
        assertEquals(1, patients.size());
        assertEquals("John", patients.get(0).getGivenName());
    }

    @Test
    void fetchSinglePatientsFromEndpoint_ShouldReturnPatient() throws Exception {
        // Arrange
        String jsonResponse = "{\"patientId\":1,\"givenName\":\"John\",\"familyName\":\"Doe\"}";
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenReturn(jsonResponse);

        // Act
        Patient patient = docNotesService.fetchSinglePatientsFromEndpoint(1);

        // Assert
        assertEquals("John", patient.getGivenName());
    }

    @Test
    void addDocNoteService_ShouldAddNewDocNotes() {
        // Arrange
        Integer patientId = 1;
        String note = "Patient has high blood pressure";
        when(docNotesRepository.findByPatientId(patientId)).thenReturn(Optional.empty());

        // Act
        String result = docNotesService.addDocNoteService(patientId, model, note);

        // Assert
        verify(docNotesRepository, times(1)).save(any(DocNotes.class));
        assertEquals("redirect:/docNotes/patient/1", result);
    }

    @Test
    void addDocNoteService_ShouldUpdateExistingDocNotes() {
        // Arrange
        Integer patientId = 1;
        String note = "Patient has high blood pressure";
        DocNotes existingDocNotes = new DocNotes();
        existingDocNotes.setPatientId(patientId);
        existingDocNotes.setNotes(new ArrayList<>(List.of("Patient is healthy")));
        when(docNotesRepository.findByPatientId(patientId)).thenReturn(Optional.of(existingDocNotes));

        // Act
        String result = docNotesService.addDocNoteService(patientId, model, note);

        // Assert
        verify(docNotesRepository, times(1)).save(any(DocNotes.class));
        assertEquals(2, existingDocNotes.getNotes().size());
        assertEquals("redirect:/docNotes/patient/1", result);
    }

    @Test
    void showPatientDocNotes_ShouldReturnPatientNotes() {
        // Arrange
        Integer patientId = 1;
        DocNotes docNotes = new DocNotes();
        docNotes.setNotes(new ArrayList<>(List.of("Note 1", "Note 2")));
        when(docNotesRepository.findByPatientId(patientId)).thenReturn(Optional.of(docNotes));
        when(restTemplate.getForObject(any(String.class), eq(String.class)))
                .thenReturn("{\"patientId\":1,\"givenName\":\"John\",\"familyName\":\"Doe\"}");

        // Act
        String view = docNotesService.showPatientDocNotes(patientId, model);

        // Assert
        verify(model).addAttribute("currentPatientDocNotes", docNotes.getNotes());
        verify(model).addAttribute("patientFullName", "John Doe");
        assertEquals("DocNotes/patientNotes", view);
    }

    @Test
    void removeDoctorNote_ShouldRemoveNote() {
        // Arrange
        Integer patientId = 1;
        String noteToRemove = "Remove this note";
        DocNotes docNotes = new DocNotes();
        docNotes.setNotes(new ArrayList<>(List.of("Keep this note", "Remove this note")));
        when(docNotesRepository.findByPatientId(patientId)).thenReturn(Optional.of(docNotes));

        // Act
        String result = docNotesService.removeDoctorNote(patientId, noteToRemove, model);

        // Assert
        verify(docNotesRepository, times(1)).save(any(DocNotes.class));
        assertEquals(1, docNotes.getNotes().size());
        assertEquals("Keep this note", docNotes.getNotes().get(0));
        assertEquals("redirect:/docNotes/patient/1", result);
    }

    @Test
    void updateDoctorNote_ShouldUpdateExistingNote() {
        // Arrange
        Integer patientId = 1;
        String oldNote = "Old note";
        String newNote = "Updated note";
        DocNotes docNotes = new DocNotes();
        docNotes.setNotes(new ArrayList<>(List.of(oldNote)));
        when(docNotesRepository.findByPatientId(patientId)).thenReturn(Optional.of(docNotes));

        // Act
        String result = docNotesService.updateDoctorNote(patientId, oldNote, newNote, model);

        // Assert
        verify(docNotesRepository, times(1)).save(any(DocNotes.class));
        assertEquals(1, docNotes.getNotes().size());
        assertEquals("Updated note", docNotes.getNotes().get(0));
        assertEquals("redirect:/docNotes/patient/1", result);
    }

    @Test
    void updatePatients_ShouldDeleteDocNotes_WhenPatientDoesNotExist() {
        // Arrange
        DocNotes docNotes1 = new DocNotes();
        docNotes1.setDocNotesId(new ObjectId());
        docNotes1.setPatientId(1);

        List<DocNotes> allDocNotes = List.of(docNotes1);
        when(docNotesRepository.findAll()).thenReturn(allDocNotes);
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenReturn(null);  // Patient does not exist

        // Act
        docNotesService.updatePatients();

        // Assert
        verify(docNotesRepository, times(1)).deleteById(docNotes1.getDocNotesId());
    }
}
