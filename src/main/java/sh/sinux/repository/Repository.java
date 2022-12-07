package sh.sinux.repository;

import java.util.List;

/**
 * A repository is a storage for snippets.
 * The name of a snippet has to be unique.
 * @author sinux-l5d
 * @since 0.1.0
 */
public interface Repository {
    boolean save(String name, String content);
    String get(String name);
    boolean remove(String name);
    List<String> list();
}
