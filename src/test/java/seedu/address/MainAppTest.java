package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.Config;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.storage.UserPrefsStorage;

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
    public void initPrefs_missingFile_returnsDefaultPrefsAndSavesPrefs() {
        StubUserPrefsStorage storage = new StubUserPrefsStorage(tempDir.resolve("missingPrefs.json"), Optional.empty());

        UserPrefs initializedPrefs = mainApp.initPrefs(storage);

        assertEquals(new UserPrefs(), initializedPrefs);
        assertEquals(1, storage.saveCallCount);
        assertNotNull(storage.savedPrefs);
        assertEquals(initializedPrefs, storage.savedPrefs);
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

    private static class StubUserPrefsStorage implements UserPrefsStorage {
        private final Path userPrefsFilePath;
        private final Optional<UserPrefs> prefsToRead;
        private final DataLoadingException readException;
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
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) {
            saveCallCount++;
            savedPrefs = userPrefs;
        }
    }
}
