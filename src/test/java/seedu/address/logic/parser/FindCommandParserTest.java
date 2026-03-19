package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.FieldContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonPrefixedArg_throwsParseException() {
        assertParseFailure(parser, "Alice Bob", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_prefixWithEmptyValue_throwsParseException() {
        assertParseFailure(parser, " on/   ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new FieldContainsKeywordsPredicate(
                        Optional.of("Alice Bob"),
                        Optional.of("9435"),
                        Optional.of("example.com"),
                        Optional.of("Jurong West"),
                        List.of("friends", "school")));

        assertParseSuccess(parser, " on/Alice Bob ph/9435 em/example.com ad/Jurong West ot/friends ot/school",
                expectedFindCommand);
    }

}
