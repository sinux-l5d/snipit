package sh.sinux;

import picocli.CommandLine;
import picocli.CommandLine.*;
import sh.sinux.command.*;
import sh.sinux.config.Config;
import sh.sinux.config.StorageType;
import sh.sinux.repository.Repository;
import sh.sinux.repository.RepositoryProxy;

/**
 * The Main class is the entry point of the application.
 * It parses the command line arguments and executes the command by using Picocli.
 * It also initializes the configuration and the repository proxy.
 * @author sinux-l5d
 * @version 1.0
 */
@Command(name = "snipit", mixinStandardHelpOptions = true, version = "snipit 1.0", synopsisSubcommandLabel = "COMMAND",
        description = "A simple snippet manager",
        subcommands = {
                ListCommand.class,
                AddCommand.class,
                RemoveCommand.class,
                ListTagsCommand.class,
                SearchCommand.class,
                InfoCommand.class,
                ShowCommand.class,
        })
public class Main {

    /**
     * The path to the directory where the snippets are stored.
     * For the defaults, see {@link Config#fsAppDir()}
     */
    @Option(names = {"-p", "--path"}, description = "Path to the storage")
    String storagePath;

    /**
     * Program entry point.
     * Parse the command line arguments and execute the command.
     * Uses {@link Main#executionStrategy(ParseResult)} strategy to init the application before executing the command.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        var app = new Main();
        int exitCode = new CommandLine(app).setExecutionStrategy(app::executionStrategy).execute(args);
        System.exit(exitCode);
    }

    /**
     * The execution strategy is called by picocli before executing the command.
     * It calls {@link Main#init()} to initialize the configuration and the repository proxy.
     * @param parseResult the result of the parsing of the command line arguments
     * @return an integer representing the exit code of the command
     */
    private int executionStrategy(ParseResult parseResult) {
        init(); // custom initialization to be done before executing any command or subcommand
        return new CommandLine.RunLast().execute(parseResult); // default execution strategy
    }

    /**
     * Initialize the configuration and the repository proxy.
     * The configuration is initialized with the storage path specified by the user (if specified).
     * The repository proxy is initialized with the storage type specified in the configuration.
     */
    public void init() {
        if (storagePath == null) {
            Config.create();
        } else {
            Config.create(StorageType.FILESYSTEM, storagePath);
        }
        RepositoryProxy.create(Config.getInstance());
    }

    public Repository repository() {
        return RepositoryProxy.getInstance();
    }
}