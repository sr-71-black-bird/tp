package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.Config;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.storage.Storage;
import seedu.address.storage.UserPrefsStorage;
import seedu.address.ui.Ui;

public class MainAppTest {

    @TempDir
    public Path tempDir;

    private final MainApp mainApp = new MainApp();

    @Test
    public void initConfig_missingFile_returnsDefaultConfigAndSavesFile() throws Exception {
        Path configFilePath = tempDir.resolve("missingConfig.json");

        Config initializedConfig = mainApp.initConfig(configFilePath);

        assertEquals(new Config(), initializedConfig);
        assertTrue(Files.exists(configFilePath));
        assertEquals(Optional.of(initializedConfig), ConfigUtil.readConfig(configFilePath));
    }

    @Test
    public void initConfig_invalidFile_returnsDefaultConfigAndOverwritesFile() throws Exception {
        Path configFilePath = tempDir.resolve("invalidConfig.json");
        Files.writeString(configFilePath, "not json");

        Config initializedConfig = mainApp.initConfig(configFilePath);

        assertEquals(new Config(), initializedConfig);
        assertEquals(Optional.of(initializedConfig), ConfigUtil.readConfig(configFilePath));
    }

    @Test
    public void initConfig_validFile_returnsExistingConfigAndPersistsFile() throws Exception {
        Path configFilePath = tempDir.resolve("validConfig.json");
        Config expectedConfig = new Config();
        expectedConfig.setLogLevel(Level.FINE);
        expectedConfig.setUserPrefsFilePath(tempDir.resolve("prefsFromConfig.json"));
        ConfigUtil.saveConfig(expectedConfig, configFilePath);

        Config initializedConfig = mainApp.initConfig(configFilePath);

        assertEquals(expectedConfig, initializedConfig);
        assertEquals(Optional.of(expectedConfig), ConfigUtil.readConfig(configFilePath));
    }

    @Test
    public void initPrefs_missingFile_returnsDefaultPrefsAndSavesPrefs() {
        StubUserPrefsStorage storage = new StubUserPrefsStorage(tempDir.resolve("missingPrefs.json"), Optional.empty());

        UserPrefs initializedPrefs = mainApp.initPrefs(storage);

        assertEquals(new UserPrefs(), initializedPrefs);
        assertEquals(1, storage.saveCallCount);
        assertNotNull(storage.savedPrefs);
        assertEquals(initializedPrefs, storage.savedPrefs);
    }

    @Test
    public void initPrefs_existingPrefs_returnsExistingPrefsAndSavesPrefs() {
        UserPrefs existingPrefs = new UserPrefs();
        existingPrefs.setAddressBookFilePath(tempDir.resolve("existingPetlog.json"));
        StubUserPrefsStorage storage = new StubUserPrefsStorage(
                tempDir.resolve("existingPrefs.json"), Optional.of(existingPrefs));

        UserPrefs initializedPrefs = mainApp.initPrefs(storage);

        assertEquals(existingPrefs, initializedPrefs);
        assertEquals(1, storage.saveCallCount);
        assertEquals(existingPrefs, storage.savedPrefs);
    }

    @Test
    public void initPrefs_invalidFile_returnsDefaultPrefsAndSavesPrefs() {
        StubUserPrefsStorage storage = new StubUserPrefsStorage(tempDir.resolve("invalidPrefs.json"),
                new DataLoadingException(new IOException("bad prefs")));

        UserPrefs initializedPrefs = mainApp.initPrefs(storage);

        assertEquals(new UserPrefs(), initializedPrefs);
        assertEquals(1, storage.saveCallCount);
        assertNotNull(storage.savedPrefs);
        assertEquals(initializedPrefs, storage.savedPrefs);
    }

    @Test
    public void initPrefs_saveThrowsIoException_returnsPrefsWithoutThrowing() {
        UserPrefs existingPrefs = new UserPrefs();
        StubUserPrefsStorage storage = new StubUserPrefsStorage(
                tempDir.resolve("prefsWithSaveFailure.json"), Optional.of(existingPrefs));
        storage.throwOnSave = true;

        UserPrefs initializedPrefs = mainApp.initPrefs(storage);

        assertEquals(existingPrefs, initializedPrefs);
        assertEquals(1, storage.saveCallCount);
        assertEquals(existingPrefs, storage.savedPrefs);
    }

    @Test
    public void start_withStubUi_delegatesToUiStart() {
        StubUi stubUi = new StubUi();
        mainApp.ui = stubUi;

        mainApp.start(null);

        assertEquals(1, stubUi.startCallCount);
        assertNull(stubUi.lastStage);
    }

    @Test
    public void stop_successfulSave_callsSaveAddressBookAndSaveUserPrefs() {
        mainApp.model = new ModelManager();
        StubStorage storage = new StubStorage(
                tempDir.resolve("stopSuccessPetlog.json"), tempDir.resolve("stopSuccessPrefs.json"));
        mainApp.storage = storage;

        mainApp.stop();

        assertEquals(1, storage.saveAddressBookCallCount);
        assertEquals(1, storage.saveUserPrefsCallCount);
        assertNotNull(storage.savedAddressBook);
        assertNotNull(storage.savedUserPrefs);
    }

    @Test
    public void stop_saveThrowsIoException_stillAttemptsBothSaveOperations() {
        mainApp.model = new ModelManager();
        StubStorage storage = new StubStorage(
                tempDir.resolve("stopFailurePetlog.json"), tempDir.resolve("stopFailurePrefs.json"));
        storage.throwOnSaveAddressBook = true;
        storage.throwOnSaveUserPrefs = true;
        mainApp.storage = storage;

        mainApp.stop();

        assertEquals(1, storage.saveAddressBookCallCount);
        assertEquals(1, storage.saveUserPrefsCallCount);
    }

    private static class StubUserPrefsStorage implements UserPrefsStorage {
        private final Path userPrefsFilePath;
        private final Optional<UserPrefs> prefsToRead;
        private final DataLoadingException readException;
        private boolean throwOnSave;
        private ReadOnlyUserPrefs savedPrefs;
        private int saveCallCount;

        StubUserPrefsStorage(Path userPrefsFilePath, Optional<UserPrefs> prefsToRead) {
            this.userPrefsFilePath = userPrefsFilePath;
            this.prefsToRead = prefsToRead;
            this.readException = null;
        }

        StubUserPrefsStorage(Path userPrefsFilePath, DataLoadingException readException) {
            this.userPrefsFilePath = userPrefsFilePath;
            this.prefsToRead = Optional.empty();
            this.readException = readException;
        }

        @Override
        public Path getUserPrefsFilePath() {
            return userPrefsFilePath;
        }

        @Override
        public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
            if (readException != null) {
                throw readException;
            }
            return prefsToRead;
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
            saveCallCount++;
            savedPrefs = userPrefs;
            if (throwOnSave) {
                throw new IOException("simulated save failure");
            }
        }
    }

    private static class StubUi implements Ui {
        private javafx.stage.Stage lastStage;
        private int startCallCount;

        @Override
        public void start(javafx.stage.Stage primaryStage) {
            startCallCount++;
            lastStage = primaryStage;
        }
    }

    private static class StubStorage implements Storage {
        private final Path addressBookFilePath;
        private final Path userPrefsFilePath;
        private boolean throwOnSaveAddressBook;
        private boolean throwOnSaveUserPrefs;
        private ReadOnlyAddressBook savedAddressBook;
        private ReadOnlyUserPrefs savedUserPrefs;
        private int saveAddressBookCallCount;
        private int saveUserPrefsCallCount;

        StubStorage(Path addressBookFilePath, Path userPrefsFilePath) {
            this.addressBookFilePath = addressBookFilePath;
            this.userPrefsFilePath = userPrefsFilePath;
        }

        @Override
        public Path getAddressBookFilePath() {
            return addressBookFilePath;
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook() {
            return Optional.empty();
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) {
            return Optional.empty();
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
            saveAddressBookCallCount++;
            savedAddressBook = addressBook;
            if (throwOnSaveAddressBook) {
                throw new IOException("simulated address book save failure");
            }
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
            saveAddressBook(addressBook);
        }

        @Override
        public Path getUserPrefsFilePath() {
            return userPrefsFilePath;
        }

        @Override
        public Optional<UserPrefs> readUserPrefs() {
            return Optional.empty();
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
            saveUserPrefsCallCount++;
            savedUserPrefs = userPrefs;
            if (throwOnSaveUserPrefs) {
                throw new IOException("simulated user prefs save failure");
            }
        }
    }
}
