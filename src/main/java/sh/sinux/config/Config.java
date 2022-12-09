package sh.sinux.config;

import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Configuration of the application.
 * It's a singleton class, see {@link #create()}, {@link #create(StorageType, String)} or {@link #getInstance()}.
 * @author sinux-l5d
 * @since 1.0
 */
public class Config {

    /** The singleton instance */
    private static Config instance;

    /** The storage path (string relevant to the {@link sh.sinux.repository.Repository}) */
    private final String storagePath;

    /** The storage type. Determines the Repository used by {@link sh.sinux.repository.RepositoryProxy} */
    private final StorageType storageType;

    // Constructors

    /**
     * Creates a new Config instance.
     * @param storageType the storage type
     * @param storagePath the storage path
     */
    private Config( StorageType storageType, String storagePath) {
        this.storageType = storageType;
        this.storagePath = storagePath;
    }

    /**
     * Creates a new instance of the Config class.
     * If the instance already exists, it will be returned, which means that the
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
     * <pre>{@code
     * 1. XDG_CONFIG_HOME/snapit
     * 2. %APPDATA%/snapit
     * 3. HOME/.snapit
     * }</pre>
     * @return Path to the application directory
     * @throws RuntimeException if the application directory is not writable
     */
    private static Path fsAppDir() throws RuntimeException {
        // Try Unix first
        String configDir = System.getenv("XDG_CONFIG_HOME");
        String isDotfile = "";

        if (configDir == null) // Windows
            configDir = System.getenv("APPDATA");
        if (configDir == null) { // Fallback
            configDir = System.getProperty("user.home");
            isDotfile = ".";
        }

        // test if the path is writable , otherwise throw exception
        if (!FileSystems.getDefault().getPath(configDir).toFile().canWrite())
            throw new RuntimeException("Cannot write to the application directory: " + configDir);

        return FileSystems.getDefault().getPath(configDir, isDotfile + "snipit");
    }

    /**
     * Returns the singleton instance.
     * May return null if the instance has not been created yet with {@link #create()} methods family.
     * @return Config instance
     */
    public static Config getInstance() {
        return instance;
    }

    // Methods

    /**
     * Get the storage path.
     * @return the storage path
     */
    public String getStoragePath() {
        return storagePath;
    }

    /**
     * Get the storage type.
     * @return the storage type
     */
    public StorageType getStorageType() {
        return storageType;
    }
}
