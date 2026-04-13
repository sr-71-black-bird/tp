package seedu.address.commons.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class DataLoadingExceptionTest {

    @Test
    public void constructor_withCause_setsCause() {
        IOException cause = new IOException("disk failure");
        DataLoadingException exception = new DataLoadingException(cause);

        assertEquals(cause, exception.getCause());
    }
}
