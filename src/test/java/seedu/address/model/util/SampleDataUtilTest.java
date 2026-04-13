package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.pet.Pet;
import seedu.address.model.service.Service;
import seedu.address.model.session.Session;
import seedu.address.model.tag.Tag;

public class SampleDataUtilTest {

    @Test
    public void getSampleServices_returnsExpectedServices() {
        Service[] actualServices = SampleDataUtil.getSampleServices();
        Service[] expectedServices = new Service[] {
            new Service("Base service charge", 20.0),
            new Service("Shampoo", 30.0),
            new Service("Fur trim", 25.0),
            new Service("Nail trim", 10.0),
            new Service("Walk", 15.0)
        };

        assertArrayEquals(expectedServices, actualServices);
    }

    @Test
    public void getSamplePersons_returnsExpectedData() {
        Person[] samplePersons = SampleDataUtil.getSamplePersons();

        assertEquals(6, samplePersons.length);
        assertEquals("Alex Yeoh", samplePersons[0].getName().toString());
        assertEquals("Roy Balakrishnan", samplePersons[samplePersons.length - 1].getName().toString());
        assertTrue(samplePersons[0].getPetCount() > 0);
    }

    @Test
    public void getSamplePersons_sessionsHaveCorrectFeeAndSortedChronologically() {
        Person[] samplePersons = SampleDataUtil.getSamplePersons();

        for (Person person : samplePersons) {
            for (Pet pet : person.getPetList()) {
                List<Session> sessions = pet.getSessions();
                for (Session session : sessions) {
                    double expectedFee = session.getServices().stream().mapToDouble(Service::getCost).sum();
                    assertEquals(expectedFee, session.getFee(), 1e-9);
                }

                for (int i = 1; i < sessions.size(); i++) {
                    Session previousSession = sessions.get(i - 1);
                    Session currentSession = sessions.get(i);
                    assertFalse(currentSession.getStartDateTime().isBefore(previousSession.getStartDateTime()));
                }
            }
        }
    }

    @Test
    public void getSampleAddressBook_returnsAddressBookWithSampleOwnersAndServices() {
        ReadOnlyAddressBook sampleAddressBook = SampleDataUtil.getSampleAddressBook();

        assertEquals(Arrays.asList(SampleDataUtil.getSamplePersons()), sampleAddressBook.getPersonList());
        assertEquals(Arrays.asList(SampleDataUtil.getSampleServices()), sampleAddressBook.getServiceList());
    }

    @Test
    public void getTagSet_duplicateAndWhitespaceTags_normalizedAndDeduplicated() {
        Set<Tag> tags = SampleDataUtil.getTagSet(" member ", "MEMBER", "vip");

        assertEquals(Set.of(new Tag("member"), new Tag("vip")), tags);
    }
}
