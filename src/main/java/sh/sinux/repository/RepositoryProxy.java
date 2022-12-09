package sh.sinux.repository;

import sh.sinux.Snippet;
import sh.sinux.config.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * The RepositoryProxy choose which repository to use based on the config.
 * This is a singleton class, see {@link #create(Config)} and {@link #getInstance()}.
 * It performs optimizations when it can.
 * @author sinux-l5d
 * @since 1.0
 */
public class RepositoryProxy implements Repository {

    private static RepositoryProxy instance;

    private final Repository repository;
    /**
     * Store snippets unique names to prevent duplicates.
     */
    private final ArrayList<String> snippetsName = new ArrayList<>();

    private RepositoryProxy(Config config) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (config.getStorageType()) {
            case FILESYSTEM -> repository = new FilesystemRepository(config);
            default -> throw new RuntimeException("Unknown storage type: " + config.getStorageType());
        }
        snippetsName.addAll(repository.listNames());
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
    public boolean save(String name, String content, String[] tags) {
        // unique name
        if (name == null || snippetsName.contains(name)) return false;

        var ok = repository.save(name, content, tags);
        if (ok) snippetsName.add(name);
        return ok;
    }

    @Override
    public Snippet get(String name) {
        return repository.get(name);
    }

    @Override
    public boolean remove(String name) {
        var ok = repository.remove(name);
        if (ok) snippetsName.remove(name);
        return ok;
    }

    /**
     * List all the snippets name in the repository.
     *
     * @return a list of snippet names
     */
    @Override
    public List<String> listNames() {
        return snippetsName;
    }

    @Override
    public List<String> listTags() {
        return repository.listTags();
    }

    @Override
    public List<Snippet> searchAll(String query) {
        return repository.searchAll(query);
    }

    @Override
    public List<Snippet> searchName(String query) {
        // Not using the cached list of snippets because we return full Snippets objects
        // Meaning, the inner repository could have a special method to retrieve every snippet at once.
        // So we let the inner repository do the job.
        return repository.searchName(query);
    }

    @Override
    public List<Snippet> searchContent(String query) {
        return repository.searchContent(query);
    }

    @Override
    public List<Snippet> searchTags(String query) {
        return repository.searchTags(query);
    }
}
