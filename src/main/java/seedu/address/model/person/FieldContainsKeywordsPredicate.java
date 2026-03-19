package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person} matches all provided field search strings.
 */
public class FieldContainsKeywordsPredicate implements Predicate<Person> {
    private final Optional<String> ownerNameKeyword;
    private final Optional<String> phoneKeyword;
    private final Optional<String> emailKeyword;
    private final Optional<String> addressKeyword;
    private final List<String> tagKeywords;

    /**
     * Constructs a predicate that matches persons whose provided fields contain the corresponding search strings.
     */
    public FieldContainsKeywordsPredicate(Optional<String> ownerNameKeyword, Optional<String> phoneKeyword,
                                          Optional<String> emailKeyword, Optional<String> addressKeyword,
                                          List<String> tagKeywords) {
        requireNonNull(ownerNameKeyword);
        requireNonNull(phoneKeyword);
        requireNonNull(emailKeyword);
        requireNonNull(addressKeyword);
        requireNonNull(tagKeywords);
        this.ownerNameKeyword = ownerNameKeyword;
        this.phoneKeyword = phoneKeyword;
        this.emailKeyword = emailKeyword;
        this.addressKeyword = addressKeyword;
        this.tagKeywords = List.copyOf(tagKeywords);
    }

    @Override
    public boolean test(Person person) {
        return matchesField(person.getName().fullName, ownerNameKeyword)
                && matchesField(person.getPhone().value, phoneKeyword)
                && matchesField(person.getEmail().value, emailKeyword)
                && matchesField(person.getAddress().value, addressKeyword)
                && matchesTags(person.getTags());
    }

    private static boolean matchesField(String fieldValue, Optional<String> keyword) {
        return keyword.map(value -> containsIgnoreCase(fieldValue, value)).orElse(true);
    }

    private boolean matchesTags(Iterable<Tag> tags) {
        return tagKeywords.stream()
                .allMatch(tagKeyword -> containsInAnyTag(tags, tagKeyword));
    }

    private static boolean containsInAnyTag(Iterable<Tag> tags, String tagKeyword) {
        for (Tag tag : tags) {
            if (containsIgnoreCase(tag.tagName, tagKeyword)) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsIgnoreCase(String fieldValue, String keyword) {
        return fieldValue.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FieldContainsKeywordsPredicate)) {
            return false;
        }

        FieldContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (FieldContainsKeywordsPredicate) other;
        return ownerNameKeyword.equals(otherNameContainsKeywordsPredicate.ownerNameKeyword)
                && phoneKeyword.equals(otherNameContainsKeywordsPredicate.phoneKeyword)
                && emailKeyword.equals(otherNameContainsKeywordsPredicate.emailKeyword)
                && addressKeyword.equals(otherNameContainsKeywordsPredicate.addressKeyword)
                && tagKeywords.equals(otherNameContainsKeywordsPredicate.tagKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("ownerNameKeyword", ownerNameKeyword)
                .add("phoneKeyword", phoneKeyword)
                .add("emailKeyword", emailKeyword)
                .add("addressKeyword", addressKeyword)
                .add("tagKeywords", tagKeywords)
                .toString();
    }
}
