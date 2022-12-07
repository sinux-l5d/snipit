package sh.sinux.repository;

import sh.sinux.config.Config;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * FilesystemRepository is a repository that stores snippets in the filesystem.
 * It uses the {@link sh.sinux.config.Config#getStoragePath()} as the root directory.
 * It assumes that the storage type is {@link sh.sinux.config.StorageType#FILESYSTEM}.
 * It is stored like this:
 * <pre>{@code
 * app-dir/
 * - /snippets/
 *   - /snippet-unique-name/
 *     - /content (the content of the snippet)
 *     - /tags (the tags of the snippet, separated by a newline)
 * }</pre>
 *
 * @author sinux-l5d
 * @since 0.1.0
 */
public class FilesystemRepository implements Repository {
    Config config;
    Path root;

    public FilesystemRepository(Config config) {
        this.config = config;
        this.root = FileSystems.getDefault().getPath(config.getStoragePath());

        if (!root.toFile().exists())
            if(!root.toFile().mkdirs())
                throw new RuntimeException("Cannot create the root directory: " + root);

    }

    @Override
    public boolean save(String name, String content) {
        return false;
    }

    @Override
    public String get(String name) {
        return null;
    }

    @Override
    public boolean remove(String name) {
        return false;
    }

    /**
     * List all the snippets in the repository.
     * This read the directory in {@link sh.sinux.config.Config#getStoragePath()}/snippets
     * @return a list of snippet names
     */
    @Override
    public List<String> list() {
        String[] snippets = root.resolve("snippets").toFile().list();
        if (snippets == null) return new ArrayList<>();
        return Arrays.asList(snippets);
    }
}
