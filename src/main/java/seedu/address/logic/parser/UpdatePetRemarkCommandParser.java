package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_REMARK;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UpdatePetRemarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.pet.PetRemark;

/**
 * Parses input arguments and creates a new UpdatePetRemarkCommand object
 */
public class UpdatePetRemarkCommandParser {
    /**
     * Parses the given {@code String} of arguments in the context of the UpdatePetRemarkCommand
     * and returns an UpdatePetRemarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public UpdatePetRemarkCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_OWNER_INDEX, PREFIX_PET_INDEX, PREFIX_PET_REMARK);

        if (!ParserUtil.arePrefixesPresent(argMultimap, PREFIX_OWNER_INDEX, PREFIX_PET_INDEX, PREFIX_PET_REMARK)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    UpdatePetRemarkCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_OWNER_INDEX, PREFIX_PET_INDEX, PREFIX_PET_REMARK);
        Index ownerIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_OWNER_INDEX).get());
        Index petIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_PET_INDEX).get());
        PetRemark petRemark = ParserUtil.parsePetRemark(argMultimap.getValue(PREFIX_PET_REMARK).get());
        return new UpdatePetRemarkCommand(ownerIndex, petIndex, petRemark.toString());
    }

}
