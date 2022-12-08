package sh.sinux.command;

import picocli.CommandLine.*;
import sh.sinux.repository.RepositoryProxy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.Callable;

@Command(name = "add", description = "Add a new snippet", mixinStandardHelpOptions = true)
public class AddCommand implements Callable<Integer> {


    @Parameters(index = "0", description = "The file to add as a snippet", paramLabel = "FILE")
    private File input;

    @Option(names = {"-n", "--name"}, description = "The name of the snippet")
    private String name;

    @Option(names = {"-t", "--tag"}, description = "The tags of the snippet")
    private String[] tags;

    @Override
    public Integer call() {
        var repo = RepositoryProxy.getInstance();
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
