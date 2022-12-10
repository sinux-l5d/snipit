package sh.sinux.command;

import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ArgGroup;
import sh.sinux.Main;
import sh.sinux.Snippet;
import sh.sinux.repository.Repository;

import java.util.List;

/**
 * The Search command is the user api to search snippets.
 * It prints the name of the snippets matching the query
 * @author sinux-l5d
 * @version 1.0
 */
@Command(name = "search", description = "Search in snippets name, content and tags", mixinStandardHelpOptions = true)
public class SearchCommand implements Runnable {

    @ParentCommand
    private Main main;
    /** The query to search for */
    @Parameters(index = "0", description = "The query to search")
    String query;

    /**
     * Eventually specify where to search (name, tags, content)
     * The default is to search everywhere.
     * This is a group of mutually exclusive options.
     */
    @ArgGroup()
    SearchOptions options;
    private static class SearchOptions {
        /** Search in snippet name */
        @Option(names = {"-n", "--name"}, description = "Search in snippet name")
        boolean name;

        /** Search in snippet tags */
        @Option(names = {"-c", "--content"}, description = "Search in snippet content")
        boolean content;

        /** Search in snippet content */
        @Option(names = {"-t", "--tags"}, description = "Search in snippet tags")
        boolean tags;

        /** Search everywhere (default) */
        @Option(names = {"-a", "--all"}, description = "Search in snippet name, content and tags (default)")
        boolean all;
    }

    /**
     * The run method is called by picocli when the command is executed.
     * It prints the name of the snippets matching the query
     */
    @Override
    public void run() {
        List<Snippet> snippets;
        Repository repo = main.repository();
        if (options == null || options.all) {
            snippets = repo.searchAll(query);
        } else if (options.name) {
            snippets = repo.searchName(query);
        } else if (options.content) {
            snippets = repo.searchContent(query);
        } else if (options.tags) {
            snippets = repo.searchTags(query);
        } else {
            snippets = repo.searchAll(query);
        }

        if (snippets.isEmpty()) {
            System.out.println("No snippets found");
            return;
        }
        System.out.println("Snippets found:");
        for (var snippet : snippets) {
            System.out.println("  - " + snippet.name());
        }
    }
}
