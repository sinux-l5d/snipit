package sh.sinux.repository;

import sh.sinux.Snippet;

import java.util.List;

/**
 * A repository is a storage for snippets.
 * The name of a snippet has to be unique.
 * @author sinux-l5d
 * @since 0.1.0
 */
public interface Repository {
    boolean save(String name, String content, String[] tags);
    Snippet get(String name);
    boolean remove(String name);
    /**
     * List all the snippets name in the repository.
     * @return a list of snippet names
     */
    List<String> list();

    List<String> listTags();

    List<Snippet> search(String query);
}
