package sh.sinux.config;

/**
 * The storage type determines the type of storage used by the application.
 * @author sinux-l5d
 * @since 1.0
 */
public enum StorageType {
    /** The filesystem storage type */
    FILESYSTEM("filesystem");

    /** The name of the storage type */
    private final String typeName;

    /**
     * Creates a new StorageType instance.
     * @param type the name of the storage type
     */
    StorageType(String type) {
        this.typeName = type;
    }

    /**
     * Returns the name of the storage type.
     * @return the name of the storage type
     */
    public String toString() {
        return typeName;
    }

}
