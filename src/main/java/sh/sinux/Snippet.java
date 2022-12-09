package sh.sinux;

public record Snippet(String name, String content, String location, String[] tags) {
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
