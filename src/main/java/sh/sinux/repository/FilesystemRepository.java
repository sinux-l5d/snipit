package sh.sinux.repository;

import sh.sinux.Snippet;
import sh.sinux.config.Config;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
 *     - /content (the content file of the snippet)
 *     - /tags (the tags of the snippet file, separated by a newline)
 * }</pre>
 *
 * @author sinux-l5d
 * @since 0.1.0
 */
public class FilesystemRepository implements Repository {
    private final Path root;

    private static final String SNIPPETS_DIR = "snippets";

    public FilesystemRepository(Config config) {
        this.root = FileSystems.getDefault().getPath(config.getStoragePath());

        if (!root.toFile().exists())
            if (!root.toFile().mkdirs())
                throw new RuntimeException("Cannot create the root directory: " + root);

    }

    @Override
    public boolean save(String name, String content, String[] tags) {
        if (name == null || name.isBlank()) return false;
        var snippetDir = root.resolve(SNIPPETS_DIR).resolve(name);
        // No duplicate name
        if (snippetDir.toFile().exists()) return false;
        System.out.println("Doesn't exist yet");

        // Create the snippet directory
        if (!snippetDir.toFile().mkdirs()) return false;
        System.out.println("Created the snippet directory");

        // Files to write
        var contentFile = snippetDir.resolve("content");
        var tagsFile = snippetDir.resolve("tags");

        // Create the files
        try {
            if (!contentFile.toFile().createNewFile()) return false;
            if (!tagsFile.toFile().createNewFile()) return false;
        } catch (Exception e) {
            e.printStackTrace();
            remove(name);
            return false;
        }
        System.out.println("Created the files");

        // Write the content
        try {
            Files.writeString(contentFile, content);
            Files.writeString(tagsFile, String.join("\n", tags));
        } catch (Exception e) {
            e.printStackTrace();
            remove(name);
            return false;
        }
        System.out.println("Wrote the content");

        return true;
    }

    @Override
    public Snippet get(String name) {
        if (name == null || name.isBlank()) return null;
        var snippetDir = root.resolve(SNIPPETS_DIR).resolve(name);
        if (!snippetDir.toFile().exists()) return null;

        var contentFile = snippetDir.resolve("content");
        var tagsFile = snippetDir.resolve("tags");


        if (!contentFile.toFile().exists() || !tagsFile.toFile().exists()) return null;

        String content;
        String[] tags;
        try {
            content = Files.readString(contentFile);
            tags = Files.readString(tagsFile).split("\n");
        } catch (Exception e) {
            return null;
        }

        return new Snippet(name, content, tags);
    }

    @Override
    public boolean remove(String name) {
        if (name == null || name.isBlank()) return false;
        var snippetDir = root.resolve(SNIPPETS_DIR).resolve(name);
        if (!snippetDir.toFile().exists()) return false;

        try {
            Files.walk(snippetDir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * List all the snippets in the repository.
     * This read the directory in {@link sh.sinux.config.Config#getStoragePath()}/snippets
     *
     * @return a list of snippet names
     */
    @Override
    public List<String> list() {
        String[] snippets = root.resolve(SNIPPETS_DIR).toFile().list();
        if (snippets == null) return new ArrayList<>();
        return Arrays.asList(snippets);
    }

    @Override
    public List<String> listTags() {
        return null;
    }

    @Override
    public List<Snippet> search(String query) {
        return null;
    }
}
