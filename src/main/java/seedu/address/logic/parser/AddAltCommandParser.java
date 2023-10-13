package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LINKEDIN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SECONDARY_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddAltCommand;
import seedu.address.logic.commands.AddAltCommand.AddAltPersonDescriptor;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddAltCommand object
 */
public class AddAltCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddAltCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_LINKEDIN, PREFIX_SECONDARY_EMAIL, PREFIX_TELEGRAM);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        if (!arePrefixesPresent(argMultimap, PREFIX_LINKEDIN, PREFIX_SECONDARY_EMAIL, PREFIX_TELEGRAM)) {
            //|| !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAltCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_LINKEDIN, PREFIX_SECONDARY_EMAIL, PREFIX_TELEGRAM);
        AddAltPersonDescriptor addAltPersonDescriptor = new AddAltPersonDescriptor();
        if (argMultimap.getValue(PREFIX_LINKEDIN).isPresent()) {
            addAltPersonDescriptor.setLinkedin(ParserUtil.parseLinkedin(argMultimap.getValue(PREFIX_LINKEDIN).get()));
        }
        if (argMultimap.getValue(PREFIX_SECONDARY_EMAIL).isPresent()) {
            addAltPersonDescriptor.setSecondaryEmail(ParserUtil.parseEmail(argMultimap
                    .getValue(PREFIX_SECONDARY_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_TELEGRAM).isPresent()) {
            addAltPersonDescriptor.setTelegram(ParserUtil.parseTelegram(argMultimap.getValue(PREFIX_TELEGRAM).get()));
        }

        return new AddAltCommand(index, addAltPersonDescriptor);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
