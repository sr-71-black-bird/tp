package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.pet.Pet;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<JsonAdaptedPet> pets = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("pets") List<JsonAdaptedPet> pets) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (pets != null) {
            this.pets.addAll(pets);
        }
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        pets.addAll(source.getPets().stream()
                .map(JsonAdaptedPet::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        final Name modelName = parseRequiredField(name, Name.class, Name::isValidName, Name.MESSAGE_CONSTRAINTS,
                Name::new);
        final Phone modelPhone = parseRequiredField(phone, Phone.class, Phone::isValidPhone, Phone.MESSAGE_CONSTRAINTS,
                Phone::new);
        final Email modelEmail = parseRequiredField(email, Email.class, Email::isValidEmail, Email.MESSAGE_CONSTRAINTS,
                Email::new);
        final Address modelAddress = parseRequiredField(address, Address.class, Address::isValidAddress,
                Address.MESSAGE_CONSTRAINTS, Address::new);

        final List<Pet> personPets = new ArrayList<>();
        for (JsonAdaptedPet pet : pets) {
            personPets.add(pet.toModelType());
        }

        final Set<Tag> modelTags = new HashSet<>(personTags);
        final Set<Pet> modelPets = new LinkedHashSet<>(personPets);
        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelTags, modelPets);
    }

    private static <T> T parseRequiredField(String rawValue,
                                            Class<?> fieldType,
                                            Predicate<String> validator,
                                            String constraintMessage,
                                            Function<String, T> constructor) throws IllegalValueException {
        if (rawValue == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, fieldType.getSimpleName()));
        }
        if (!validator.test(rawValue)) {
            throw new IllegalValueException(constraintMessage);
        }
        return constructor.apply(rawValue);
    }

}
