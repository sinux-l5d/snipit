package sh.sinux.repository;

import sh.sinux.Snippet;

import java.util.List;

/**
 * A repository is a storage for snippets.
 * The name of a snippet has to be unique.
 * @author sinux-l5d
 * @since 1.0
 */
public interface Repository {

    /**
     * Adds a snippet to the repository.
     * @param name the snippet name, has to be unique
     * @param content the snippet content
     * @param tags the snippet tags (can be empty)
     * @return true if the snippet was added, false if the snippet already exists or an error occurred
     */
    boolean save(String name, String content, String[] tags);

    /**
     * Gets a snippet from the repository.
     * @param name the unique name of the snippet
     * @return the snippet if it exists, null otherwise
     */
    Snippet get(String name);

    /**
     * Removes a snippet from the repository.
     * @param name the snippet's unique name
     * @return true if the snippet was removed, false if the snippet was not found or an error occurred
     */
    boolean remove(String name);

    /**
     * List all the snippets name in the repository.
     * @return a list of snippet names
     */
    List<String> listNames();

    /**
     * List of all the tags used in the repository.
     * @return a list of tags
     */
    List<String> listTags();

    /**
     * Search in name, content and tags for a given query.
     * @param query a word or a phrase to search
     * @return a list of snippets
     */
    List<Snippet> searchAll(String query);

    /**
     * Search in name for a given query.
     * @param query a word or a phrase to search
     * @return a list of snippets
     */
    List<Snippet> searchName(String query);

    /**
     * Search in content for a given query.
     * @param query a word or a phrase to search
     * @return a list of snippets
     */
    List<Snippet> searchContent(String query);

    /**
     * Search in tags for a given query.
     * @param query a word or a phrase to search
     * @return a list of snippets
     */
    List<Snippet> searchTags(String query);
}
