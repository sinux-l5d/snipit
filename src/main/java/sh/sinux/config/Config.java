package sh.sinux.config;

import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Configuration class for the application.
 * It's a singleton class, see {@link #create()} methods family.
 * @author sinux-l5d
 */
public class Config {
    private static Config instance;
    private final String storagePath;
    private final StorageType storageType;

    // Constructors
    private Config( StorageType storageType, String storagePath) {
        this.storageType = storageType;
        this.storagePath = storagePath;
    }

    /**
     * Creates a new instance of the Config class.
     * if the instance already exists, it will be returned, which means that the
     * configuration requested may not match the configuration of the instance.
     * @param storageType the type of storage to use
     * @param storagePath the path to the storage (type-dependent)
     * @return Config instance
     */
    public static Config create( StorageType storageType, String storagePath) { // default singleton constructor
        if (instance == null)
            instance = new Config(storageType, storagePath);
        return instance;
    }

    /**
     * Creates a new instance of the Config class.
     * if the instance already exists, it will be returned, which means that the
     * configuration requested may not match the configuration of the instance.
     * @return Config instance
     */
    public static Config create() { // syntactic sugar
        if (instance != null) return instance; // Not needed, but avoids unnecessary method calls
        return create(StorageType.FILESYSTEM, fsAppDir().toString());
    }

    // Static methods

    /**
     * Returns the application directory in the filesystem.
     * Priority order:
     * 1. XDG_CONFIG_HOME/snapit
     * 2. %APPDATA%/snapit
     * 3. $HOME/.snapit
     * @return Path to the application directory
     */
    private static Path fsAppDir() {
        // Try Unix first
        String configDir = System.getenv("XDG_CONFIG_HOME");
        String isDotfile = "";

        if (configDir == null) // Windows
            configDir = System.getenv("%APPDATA%");
        if (configDir == null) { // Fallback
            configDir = System.getProperty("user.home");
            isDotfile = ".";
        }

        // test if the path is writable , otherwise throw exception
        if (!FileSystems.getDefault().getPath(configDir).toFile().canWrite())
            throw new RuntimeException("Cannot write to the application directory: " + configDir);

        return FileSystems.getDefault().getPath(configDir, isDotfile + "snipit");
    }

    public static Config getInstance() {
        return instance;
    }

    // Methods
    public String getStoragePath() {
        return storagePath;
    }

    public StorageType getStorageType() {
        return storageType;
    }

}
