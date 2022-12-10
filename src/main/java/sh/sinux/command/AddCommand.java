package sh.sinux.command;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Option;
import sh.sinux.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.Callable;

/**
 * The Add command is the user api to add a file as a snippet.
 * The default name of the snippet is the name of the file.
 * The user can specify a different name with the -n option.
 * The user can add tags to the snippet with the -t option (repeated multiple times).
 * @author sinux-l5d
 * @version 1.0
 */
@Command(name = "add", description = "Add a new snippet", mixinStandardHelpOptions = true)
public class AddCommand implements Callable<Integer> {

    @ParentCommand
    private Main main;

    /** The file to read from */
    @Parameters(index = "0", description = "The file to add as a snippet", paramLabel = "FILE")
    private File input;

    /** The name of the snippet (default is the file name) */
    @Option(names = {"-n", "--name"}, description = "The name of the snippet (default to the file name)")
    private String name;

    /** The tags of the snippet */
    @Option(names = {"-t", "--tag"}, description = "The tags of the snippet")
    private String[] tags;

    /**
     * The call method is called by picocli when the command is executed.
     * It reads the file and adds it to the repository.
     * @return 0 if the snippet was added, 1 if an error occurred
     */
    @Override
    public Integer call() {
        var repo = main.repository();
        String fileContent;
        try {
            fileContent = Files.readString(input.toPath());
        } catch (IOException e) {
            return 1;
        }

        if (name == null) name = input.getName();
        if (tags == null) tags = new String[0];


        var ok = repo.save(name, fileContent, tags);

        return ok ? 0 : 1;
    }
}
