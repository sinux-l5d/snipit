package sh.sinux.repository;

import sh.sinux.Snippet;
import sh.sinux.config.Config;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        // Create the snippet directory
        if (!snippetDir.toFile().mkdirs()) return false;

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

        // Write the content
        try {
            Files.writeString(contentFile, content);
            Files.writeString(tagsFile, String.join("\n", tags));
        } catch (Exception e) {
            e.printStackTrace();
            remove(name);
            return false;
        }

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

    @SuppressWarnings({"ResultOfMethodCallIgnored", "resource"})
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
    public List<String> listNames() {
        String[] snippets = root.resolve(SNIPPETS_DIR).toFile().list();
        if (snippets == null) return new ArrayList<>();
        return Arrays.asList(snippets);
    }

    private List<Snippet> listSnippets() {
        return listNames()
                .stream()
                .map(this::get)
                .toList();
    }

    @Override
    public List<String> listTags() {
        return listSnippets()
                .stream()
                .map(Snippet::tags)
                .flatMap(Arrays::stream)
                .distinct()
                .toList();

    }

    @Override
    public List<Snippet> searchAll(String query) {
        var snippets = listSnippets();

        var merge = Stream.of(
                    searchName(snippets, query),
                    searchTags(snippets, query),
                    searchContent(snippets, query)
                )
                .flatMap(Collection::stream)
                .collect(Collectors.toSet()); // no duplicate

        return merge.stream().toList();
    }

    @Override
    public List<Snippet> searchName(String query) {
        return searchName(listSnippets(), query);
    }

    private List<Snippet> searchName(List<Snippet> list, String query) {
        return list
                .stream()
                .filter(snippet -> snippet.name().contains(query))
                .toList();
    }

    @Override
    public List<Snippet> searchContent(String query) {
        return searchContent(listSnippets(), query);
    }

    private List<Snippet> searchContent(List<Snippet> list, String query) {
        return list
                .stream()
                .filter(snippet -> snippet.content().contains(query))
                .toList();
    }

    @Override
    public List<Snippet> searchTags(String query) {
        return searchTags(listSnippets(), query);
    }

    private List<Snippet> searchTags(List<Snippet> list, String query) {
        return list
                .stream()
                .filter(snippet -> Arrays.stream(snippet.tags()).anyMatch(tag -> tag.contains(query)))
                .toList();
    }
}
