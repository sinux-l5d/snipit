package sh.sinux.repository;

import sh.sinux.config.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * The RepositoryProxy choose which repository to use based on the config.
 * This is a singleton class, see {@link #create(Config)} and {@link #getInstance()}.
 * @author sinux-l5d
 * @since 0.1.0
 */
public class RepositoryProxy implements Repository {

    private static RepositoryProxy instance;

    private final Repository repository;
    private ArrayList<String> snippetsName = new ArrayList<>();

    private RepositoryProxy(Config config) {
        switch (config.getStorageType()) {
            case FILESYSTEM -> repository = new FilesystemRepository(config);
            default -> throw new RuntimeException("Unknown storage type: " + config.getStorageType());
        }
    }

    /**
     * Creates a new instance of the RepositoryProxy class.
     * if the instance already exists, it will be returned, which means that the
     * configuration requested may not match the configuration of the instance.
     * @param config the configuration to use
     * @return RepositoryProxy instance
     */
    public static RepositoryProxy create(Config config) {
        if (instance == null)
            instance = new RepositoryProxy(config);
        return instance;
    }

    public static RepositoryProxy getInstance() {
        return instance;
    }

    @Override
    public boolean save(String name, String content) {
        // unique name
        if (snippetsName.contains(name)) return false;

        var ok = repository.save(name, content);
        if (ok) snippetsName.add(name);
        return ok;
    }

    @Override
    public String get(String name) {
        return repository.get(name);
    }

    @Override
    public boolean remove(String name) {
        var ok = repository.remove(name);
        if (ok) snippetsName.remove(name);
        return ok;
    }

    @Override
    public List<String> list() {
        return repository.list();
    }
}
