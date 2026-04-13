package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedSession.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.service.Service;
import seedu.address.model.session.Session;

public class JsonAdaptedSessionTest {

    private static final String VALID_START_TIME = "2026-05-12 09:00";
    private static final String VALID_END_TIME = "2026-05-12 10:00";
    private static final double VALID_FEE = 45.00;

    private static final JsonAdaptedService VALID_SHAMPOO = new JsonAdaptedService("Shampoo", 30.00);
    private static final JsonAdaptedService VALID_WALK = new JsonAdaptedService("Walk", 15.00);

    @Test
    public void toModelType_validSessionDetails_returnsSession() throws Exception {
        JsonAdaptedSession session = new JsonAdaptedSession(
                VALID_START_TIME, VALID_END_TIME, VALID_FEE, List.of(VALID_SHAMPOO, VALID_WALK));

        Session expectedSession = new Session(
                VALID_START_TIME,
                VALID_END_TIME,
                VALID_FEE,
                List.of(new Service("Shampoo", 30.00), new Service("Walk", 15.00)));

        assertEquals(expectedSession, session.toModelType());
    }

    @Test
    public void toModelType_nullServices_returnsSessionWithoutServices() throws Exception {
        JsonAdaptedSession session = new JsonAdaptedSession(VALID_START_TIME, VALID_END_TIME, 0.0, null);

        Session expectedSession = new Session(VALID_START_TIME, VALID_END_TIME, 0.0, List.of());
        assertEquals(expectedSession, session.toModelType());
    }

    @Test
    public void toModelType_nullStartTime_throwsIllegalValueException() {
        JsonAdaptedSession session = new JsonAdaptedSession(null, VALID_END_TIME, VALID_FEE, List.of(VALID_SHAMPOO));
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "startTime");

        assertThrows(IllegalValueException.class, expectedMessage, session::toModelType);
    }

    @Test
    public void toModelType_nullEndTime_throwsIllegalValueException() {
        JsonAdaptedSession session = new JsonAdaptedSession(VALID_START_TIME, null, VALID_FEE, List.of(VALID_SHAMPOO));
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "endTime");

        assertThrows(IllegalValueException.class, expectedMessage, session::toModelType);
    }

    @Test
    public void toModelType_invalidDateTime_throwsIllegalValueException() {
        JsonAdaptedSession session = new JsonAdaptedSession(
                "2026-02-30 10:00", VALID_END_TIME, VALID_FEE, List.of(VALID_SHAMPOO));

        assertThrows(IllegalValueException.class, Session.MESSAGE_DATETIME_CONSTRAINTS, session::toModelType);
    }

    @Test
    public void toModelType_invalidTimeRange_throwsIllegalValueException() {
        JsonAdaptedSession session = new JsonAdaptedSession(
                "2026-05-12 11:00", "2026-05-12 10:00", VALID_FEE, List.of(VALID_SHAMPOO));

        assertThrows(IllegalValueException.class, Session.MESSAGE_INVALID_TIME_RANGE, session::toModelType);
    }

    @Test
    public void toModelType_invalidService_throwsIllegalValueException() {
        JsonAdaptedService invalidService = new JsonAdaptedService("A".repeat(31), 10.0);
        JsonAdaptedSession session = new JsonAdaptedSession(
                VALID_START_TIME, VALID_END_TIME, VALID_FEE, List.of(invalidService));

        assertThrows(IllegalValueException.class, Service.MESSAGE_CONSTRAINTS, session::toModelType);
    }

    @Test
    public void toModelType_modelRoundTrip_returnsEqualSession() throws Exception {
        Session source = new Session(VALID_START_TIME, VALID_END_TIME, VALID_FEE,
                List.of(new Service("Shampoo", 30.00), new Service("Walk", 15.00)));

        JsonAdaptedSession session = new JsonAdaptedSession(source);

        assertEquals(source, session.toModelType());
    }
}
