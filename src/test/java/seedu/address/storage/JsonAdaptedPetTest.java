package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.storage.JsonAdaptedPet.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.PetName;
import seedu.address.model.pet.PetRemark;
import seedu.address.model.pet.Species;
import seedu.address.model.session.Session;

public class JsonAdaptedPetTest {

    private static final String INVALID_NAME = "A".repeat(31);
    private static final String INVALID_SPECIES = "A".repeat(31);
    private static final String INVALID_REMARK = "A".repeat(101);

    private static final String VALID_NAME = "Buddy";
    private static final String VALID_SPECIES = "Dog";
    private static final String VALID_REMARK = "Friendly and energetic";

    private static final JsonAdaptedSession EARLIER_SESSION =
            new JsonAdaptedSession("2026-05-12 09:00", "2026-05-12 10:00", 20.0, List.of());
    private static final JsonAdaptedSession LATER_SESSION =
            new JsonAdaptedSession("2026-05-12 11:00", "2026-05-12 12:00", 30.0, List.of());

    @Test
    public void toModelType_validPetDetails_returnsPet() throws Exception {
        JsonAdaptedPet pet = new JsonAdaptedPet(
                VALID_NAME, VALID_SPECIES, VALID_REMARK, List.of(LATER_SESSION, EARLIER_SESSION));

        Pet modelPet = pet.toModelType();

        assertEquals(new PetName(VALID_NAME), modelPet.getName());
        assertEquals(new Species(VALID_SPECIES), modelPet.getSpecies());
        assertEquals(new PetRemark(VALID_REMARK), modelPet.getRemark());
        assertEquals(List.of(
                new Session("2026-05-12 09:00", "2026-05-12 10:00", 20.0, List.of()),
                new Session("2026-05-12 11:00", "2026-05-12 12:00", 30.0, List.of())),
                modelPet.getSessions());
    }

    @Test
    public void toModelType_nullSessions_returnsPetWithoutSessions() throws Exception {
        JsonAdaptedPet pet = new JsonAdaptedPet(VALID_NAME, VALID_SPECIES, VALID_REMARK, null);

        Pet modelPet = pet.toModelType();
        assertTrue(modelPet.getSessions().isEmpty());
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPet pet = new JsonAdaptedPet(null, VALID_SPECIES, VALID_REMARK, List.of());
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, PetName.class.getSimpleName());

        assertThrows(IllegalValueException.class, expectedMessage, pet::toModelType);
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPet pet = new JsonAdaptedPet(INVALID_NAME, VALID_SPECIES, VALID_REMARK, List.of());

        assertThrows(IllegalValueException.class, PetName.MESSAGE_CONSTRAINTS, pet::toModelType);
    }

    @Test
    public void toModelType_nullSpecies_throwsIllegalValueException() {
        JsonAdaptedPet pet = new JsonAdaptedPet(VALID_NAME, null, VALID_REMARK, List.of());
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Species.class.getSimpleName());

        assertThrows(IllegalValueException.class, expectedMessage, pet::toModelType);
    }

    @Test
    public void toModelType_invalidSpecies_throwsIllegalValueException() {
        JsonAdaptedPet pet = new JsonAdaptedPet(VALID_NAME, INVALID_SPECIES, VALID_REMARK, List.of());

        assertThrows(IllegalValueException.class, Species.MESSAGE_CONSTRAINTS, pet::toModelType);
    }

    @Test
    public void toModelType_nullRemark_throwsIllegalValueException() {
        JsonAdaptedPet pet = new JsonAdaptedPet(VALID_NAME, VALID_SPECIES, null, List.of());
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, PetRemark.class.getSimpleName());

        assertThrows(IllegalValueException.class, expectedMessage, pet::toModelType);
    }

    @Test
    public void toModelType_invalidRemark_throwsIllegalValueException() {
        JsonAdaptedPet pet = new JsonAdaptedPet(VALID_NAME, VALID_SPECIES, INVALID_REMARK, List.of());

        assertThrows(IllegalValueException.class, PetRemark.MESSAGE_CONSTRAINTS, pet::toModelType);
    }

    @Test
    public void toModelType_invalidSession_throwsIllegalValueException() {
        JsonAdaptedSession invalidSession =
                new JsonAdaptedSession("2026-05-12 12:00", "2026-05-12 11:00", 20.0, List.of());
        JsonAdaptedPet pet = new JsonAdaptedPet(VALID_NAME, VALID_SPECIES, VALID_REMARK, List.of(invalidSession));

        assertThrows(IllegalValueException.class, Session.MESSAGE_INVALID_TIME_RANGE, pet::toModelType);
    }

    @Test
    public void toModelType_modelRoundTrip_preservesDetailsAndSessions() throws Exception {
        Pet source = new Pet(new PetName(VALID_NAME), new Species(VALID_SPECIES), new PetRemark(VALID_REMARK));
        source.addSession(new Session("2026-05-12 10:00", "2026-05-12 11:00", 15.0, List.of()));
        source.addSession(new Session("2026-05-12 08:00", "2026-05-12 09:00", 10.0, List.of()));

        JsonAdaptedPet pet = new JsonAdaptedPet(source);
        Pet modelPet = pet.toModelType();

        assertEquals(source, modelPet);
        assertEquals(source.getSessions(), modelPet.getSessions());
    }
}
