package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class EmailTest {

    private static final String LOCAL_PART_SPECIAL_CHARACTERS = "!#$%&'*+-=?^_`{|}~/.";

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Email(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }

    @Test
    public void isValidEmail() {
        // null email
        assertThrows(NullPointerException.class, () -> Email.isValidEmail(null));

        // blank email
        assertFalse(Email.isValidEmail("")); // empty string
        assertFalse(Email.isValidEmail(" ")); // spaces only

        // missing parts
        assertFalse(Email.isValidEmail("@example.com")); // missing local part
        assertFalse(Email.isValidEmail("peterjackexample.com")); // missing '@' symbol
        assertFalse(Email.isValidEmail("peterjack@")); // missing domain name

        // invalid parts
        assertFalse(Email.isValidEmail("peterjack@-")); // invalid domain name
        assertFalse(Email.isValidEmail("peterjack@exam_ple.com")); // underscore in domain name
        assertFalse(Email.isValidEmail("peter jack@example.com")); // spaces in local part
        assertFalse(Email.isValidEmail("peterjack@exam ple.com")); // spaces in domain name
        assertFalse(Email.isValidEmail("peterjack@@example.com")); // double '@' symbol
        assertFalse(Email.isValidEmail("peter@jack@example.com")); // '@' symbol in local part
        assertFalse(Email.isValidEmail("-peterjack@example.com")); // local part starts with a hyphen
        assertFalse(Email.isValidEmail("peterjack-@example.com")); // local part ends with a hyphen
        assertFalse(Email.isValidEmail("peter..jack@example.com")); // local part has two consecutive periods
        assertFalse(Email.isValidEmail("peterjack@example@com")); // '@' symbol in domain name
        assertFalse(Email.isValidEmail("peterjack@.example.com")); // domain name starts with a period
        assertFalse(Email.isValidEmail("peterjack@example.com.")); // domain name ends with a period
        assertFalse(Email.isValidEmail("peterjack@-example.com")); // domain name starts with a hyphen
        assertFalse(Email.isValidEmail("peterjack@example.com-")); // domain name ends with a hyphen
        assertFalse(Email.isValidEmail("peterjack@example.c")); // top level domain has less than two chars
        assertFalse(Email.isValidEmail("thislocalpartiswaytoolongforconstraints@example.com")); // local-part > 30
        assertFalse(Email.isValidEmail("peter_jack@very-very-very-long-example.com")); // domain > 30 chars

        // valid email
        assertTrue(Email.isValidEmail("PeterJack_1190@example.com")); // underscore in local part
        assertTrue(Email.isValidEmail("PeterJack.1190@example.com")); // period in local part
        assertTrue(Email.isValidEmail("PeterJack+1190@example.com")); // '+' symbol in local part
        assertTrue(Email.isValidEmail("PeterJack-1190@example.com")); // hyphen in local part
        assertTrue(Email.isValidEmail("a@bc")); // minimal
        assertTrue(Email.isValidEmail("test@localhost")); // alphabets only
        assertTrue(Email.isValidEmail("123@145")); // numeric local part and domain name
        assertTrue(Email.isValidEmail("a1+be.d@example1.com")); // mixture of alphanumeric and special characters
        assertTrue(Email.isValidEmail("peter_jack@very-very-very-long-example.co")); // domain length at 30 chars
        assertTrue(Email.isValidEmail("if.you.dream.it_you.can.do.it@example.com")); // long local part
        assertTrue(Email.isValidEmail("e1234567@u.nus.edu")); // more than one period in domain
        assertTrue(Email.isValidEmail("  peterjack@example.com  ")); // leading/trailing whitespace is normalized
    }

    @Test
    public void isValidEmail_localPartStartsOrEndsWithSpecialCharacter_returnsFalse() {
        for (char specialCharacter : LOCAL_PART_SPECIAL_CHARACTERS.toCharArray()) {
            assertFalse(Email.isValidEmail(specialCharacter + "peterjack@x-y"),
                    "Local part should not start with special character: " + specialCharacter);
            assertFalse(Email.isValidEmail("peterjack" + specialCharacter + "@x-y"),
                    "Local part should not end with special character: " + specialCharacter);
        }
    }

    @Test
    public void isValidEmail_localPartContainsSpecialCharacterInMiddle_returnsTrue() {
        for (char specialCharacter : LOCAL_PART_SPECIAL_CHARACTERS.toCharArray()) {
            assertTrue(Email.isValidEmail("peter" + specialCharacter + "jack@x-y"),
                    "Local part should allow special character in the middle: " + specialCharacter);
        }
    }

    @Test
    public void isValidEmail_localPartContainsOrderedPairsOfSpecialCharactersInMiddle_returnsExpectedResult() {
        for (char firstSpecialCharacter : LOCAL_PART_SPECIAL_CHARACTERS.toCharArray()) {
            for (char secondSpecialCharacter : LOCAL_PART_SPECIAL_CHARACTERS.toCharArray()) {
                String email = "a" + firstSpecialCharacter + secondSpecialCharacter + "b@x-y";
                String specialCharacterPair = "" + firstSpecialCharacter + secondSpecialCharacter;

                if ("..".equals(specialCharacterPair)) {
                    assertFalse(Email.isValidEmail(email),
                            "Local part should not allow consecutive periods in the middle");
                } else {
                    assertTrue(Email.isValidEmail(email),
                            "Local part should allow special character pair in the middle: "
                                    + specialCharacterPair);
                }
            }
        }
    }

    @Test
    public void isValidEmail_localPartContainsManySpecialCharactersInMiddle_returnsTrue() {
        assertTrue(Email.isValidEmail("a!#$%&'*+-=?^_`{|}~/.b@x-y"));
    }

    @Test
    public void constructor_whitespaceNormalized() {
        Email withExtraWhitespace = new Email("  valid@email ");
        Email normalized = new Email("valid@email");
        assertTrue(withExtraWhitespace.equals(normalized));
    }

    @Test
    public void equals() {
        Email email = new Email("valid@email");

        // same values -> returns true
        assertTrue(email.equals(new Email("valid@email")));

        // same object -> returns true
        assertTrue(email.equals(email));

        // null -> returns false
        assertFalse(email.equals(null));

        // different types -> returns false
        assertFalse(email.equals(5.0f));

        // different values -> returns false
        assertFalse(email.equals(new Email("other.valid@email")));
    }
}
