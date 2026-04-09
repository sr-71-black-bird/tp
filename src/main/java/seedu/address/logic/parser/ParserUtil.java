package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.StringUtil.normalizeWhitespace;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.pet.PetName;
import seedu.address.model.pet.PetRemark;
import seedu.address.model.pet.Species;
import seedu.address.model.service.Service;
import seedu.address.model.session.Session;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index must be a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String normalizedIndex = normalizeWhitespace(oneBasedIndex);
        if (!StringUtil.isValidOneBasedInt(normalizedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(normalizedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        String normalizedName = parseValidatedString(name, Name::isValidName, Name.MESSAGE_CONSTRAINTS);
        return new Name(normalizedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        String normalizedPhone = parseValidatedString(phone, Phone::isValidPhone, Phone.MESSAGE_CONSTRAINTS);
        return new Phone(normalizedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        String normalizedAddress = parseValidatedString(address, Address::isValidAddress, Address.MESSAGE_CONSTRAINTS);
        return new Address(normalizedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        String normalizedEmail = parseValidatedString(email, Email::isValidEmail, Email.MESSAGE_CONSTRAINTS);
        return new Email(normalizedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        String normalizedTag = parseValidatedString(tag, Tag::isValidTagName, Tag.MESSAGE_CONSTRAINTS);
        return new Tag(normalizedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String petName} into a {@code PetName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code petName} is invalid.
     */
    public static PetName parsePetName(String petName) throws ParseException {
        String normalizedPetName = parseValidatedString(petName, PetName::isValidName, PetName.MESSAGE_CONSTRAINTS);
        return new PetName(normalizedPetName);
    }

    /**
     * Parses a {@code String species} into a {@code Species}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code species} is invalid.
     */
    public static Species parseSpecies(String species) throws ParseException {
        String normalizedSpecies = parseValidatedString(species, Species::isValidSpecies, Species.MESSAGE_CONSTRAINTS);
        return new Species(normalizedSpecies);
    }

    /**
     * Parses a {@code String petRemark} into a {@code PetRemark}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code petRemark} is invalid.
     */
    public static PetRemark parsePetRemark(String petRemark) throws ParseException {
        String normalizedPetRemark = parseValidatedString(petRemark,
                PetRemark::isValidRemark, PetRemark.MESSAGE_CONSTRAINTS);
        return new PetRemark(normalizedPetRemark);
    }

    /**
     * Parses a {@code String serviceName} into a service name string.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code serviceName} is invalid.
     */
    public static String parseServiceName(String serviceName) throws ParseException {
        return parseValidatedString(serviceName, Service::isValidServiceName, Service.MESSAGE_CONSTRAINTS);
    }

    /**
     * Parses a {@code String servicePrice} into a service price.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code servicePrice} is invalid.
     */
    public static double parseServicePrice(String servicePrice) throws ParseException {
        String normalizedServicePrice = parseValidatedString(servicePrice,
                Service::isValidServicePrice, Service.MESSAGE_PRICE_CONSTRAINTS);
        return Double.parseDouble(normalizedServicePrice);
    }

    /**
     * Parses a {@code String dateTime} into a canonical session date/time string.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code dateTime} is invalid.
     */
    public static String parseDateTime(String dateTime) throws ParseException {
        return parseValidatedString(dateTime, Session::isValidDateTime, Session.MESSAGE_DATETIME_CONSTRAINTS);
    }

    /**
     * Returns true if all specified prefixes are present in {@code argumentMultimap}.
     */
    public static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        requireNonNull(argumentMultimap);
        for (Prefix prefix : prefixes) {
            if (argumentMultimap.getValue(prefix).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static String parseValidatedString(String rawValue, Predicate<String> validator, String errorMessage)
            throws ParseException {
        requireNonNull(rawValue);
        String normalizedValue = normalizeWhitespace(rawValue);
        if (!validator.test(normalizedValue)) {
            throw new ParseException(errorMessage);
        }
        return normalizedValue;
    }
}
