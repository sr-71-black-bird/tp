package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class MessagesTest {

    @Test
    public void getErrorMessageForDuplicatePrefixes_singlePrefix_returnsExpectedMessage() {
        String message = Messages.getErrorMessageForDuplicatePrefixes(PREFIX_OWNER_NAME);

        assertEquals(Messages.MESSAGE_DUPLICATE_FIELDS + "on/.", message);
    }

    @Test
    public void getErrorMessageForDuplicatePrefixes_multiplePrefixes_deduplicatesPrefixes() {
        String message = Messages.getErrorMessageForDuplicatePrefixes(
                PREFIX_OWNER_NAME, PREFIX_PHONE, PREFIX_OWNER_NAME);

        assertTrue(message.startsWith(Messages.MESSAGE_DUPLICATE_FIELDS));
        assertTrue(message.endsWith("."));
        String duplicateFields = message.substring(Messages.MESSAGE_DUPLICATE_FIELDS.length(), message.length() - 1);
        Set<String> actualFields = Arrays.stream(duplicateFields.split(" ")).collect(Collectors.toSet());
        assertEquals(Set.of("on/", "ph/"), actualFields);
    }

    @Test
    public void format_returnsPersonStringRepresentation() {
        assertEquals(BENSON.toString(), Messages.format(BENSON));
    }
}
