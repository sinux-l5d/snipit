package sh.sinux;

/**
 * A snippet is a piece of code or text.
 * It has a name, a content and tags.
 * @author sinux-l5d
 * @since 1.0
 */
public record Snippet(String name, String content, String location, String[] tags) {

    /**
     * Creates a new Snippet instance.
     * @param name the snippet name (cannot be empty)
     * @param content the snippet content (cannot be null)
     * @param tags the snippet tags
     */
    public Snippet {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        if (content == null) {
            throw new IllegalArgumentException("content cannot be null");
        }
        if (tags == null) {
            tags = new String[0];
        }
        if (location == null) {
            location = "";
        }
    }
}
