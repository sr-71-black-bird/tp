package seedu.address.logic.parser.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ParseExceptionTest {

    @Test
    public void constructor_withMessage_setsMessage() {
        ParseException exception = new ParseException("invalid command");

        assertEquals("invalid command", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void constructor_withMessageAndCause_setsMessageAndCause() {
        RuntimeException cause = new RuntimeException("root cause");
        ParseException exception = new ParseException("invalid command", cause);

        assertEquals("invalid command", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
