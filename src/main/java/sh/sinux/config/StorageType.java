package sh.sinux.config;

public enum StorageType {
    FILESYSTEM("filesystem");

    private final String typeName;

    StorageType(String type) {
        this.typeName = type;
    }

    public String toString() {
        return typeName;
    }

}
