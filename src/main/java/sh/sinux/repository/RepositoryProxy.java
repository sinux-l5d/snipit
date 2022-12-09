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

    /** The singleton instance */
    private static RepositoryProxy instance;

    /** The inner repository used by the proxy */
    private final Repository repository;

    /**
     * Store snippets unique names to perform optimizations.
     */
    private final ArrayList<String> snippetsName = new ArrayList<>();

    /**
     * Creates a new RepositoryProxy instance.
     * @param config the configuration of the application, used to determine the type of repository to use
     */
    private RepositoryProxy(Config config) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (config.getStorageType()) {
            case FILESYSTEM -> repository = new FilesystemRepository(config);
            default -> throw new RuntimeException("Unknown storage type: " + config.getStorageType());
        }
        // Load snippets names
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

    /**
     * Gets the singleton instance of the RepositoryProxy class.
     * @return the instance of the class
     */
    public static RepositoryProxy getInstance() {
        return instance;
    }

    /**
     * Save a snippet in the repository.
     * The inner repository is not called if the snippet name already exists.
     * @param name the snippet name, has to be unique
     * @param content the snippet content
     * @param tags the snippet tags (can be empty)
     * @return true if the snippet was saved, false otherwise (e.g. the snippet name is already used)
     */
    @Override
    public boolean save(String name, String content, String[] tags) {
        // unique name
        if (name == null || snippetsName.contains(name)) return false;

        var ok = repository.save(name, content, tags);
        if (ok) snippetsName.add(name);
        return ok;
    }

    /**
     * Gets a snippet from the repository.
     * @param name the name of the snippet
     * @return the snippet, or null if the snippet does not exist
     */
    @Override
    public Snippet get(String name) {
        return repository.get(name);
    }

    /**
     * Removes a snippet from the repository.
     * @param name the snippet's unique name
     * @return true if the snippet was removed, false otherwise (e.g. the snippet does not exist)
     */
    @Override
    public boolean remove(String name) {
        var ok = repository.remove(name);
        if (ok) snippetsName.remove(name);
        return ok;
    }

    /**
     * List all the snippets name in the repository.
     * @return a list of snippet names
     */
    @Override
    public List<String> listNames() {
        return snippetsName;
    }

    /**
     * List of all the tags used in the repository.
     * @return a list of tags
     */
    @Override
    public List<String> listTags() {
        return repository.listTags();
    }

    /**
     * Search in name, content and tags for a given query.
     * @param query a word or a phrase to search
     * @return a list of snippets
     */
    @Override
    public List<Snippet> searchAll(String query) {
        return repository.searchAll(query);
    }

    /**
     * Search in name for a given query.
     * @param query a word or a phrase to search
     * @return a list of snippets
     */
    @Override
    public List<Snippet> searchName(String query) {
        // Not using the cached list of snippets because we return full Snippets objects
        // Meaning, the inner repository could have a special method to retrieve every snippet at once.
        // So we let the inner repository do the job.
        return repository.searchName(query);
    }

    /**
     * Search in content for a given query.
     * @param query a word or a phrase to search
     * @return a list of snippets
     */
    @Override
    public List<Snippet> searchContent(String query) {
        return repository.searchContent(query);
    }

    /**
     * Search in tags for a given query.
     * @param query a word or a phrase to search
     * @return a list of snippets
     */
    @Override
    public List<Snippet> searchTags(String query) {
        return repository.searchTags(query);
    }
}
