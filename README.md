# SnipIt: Snippet manager

SnipIt is a snippet manager for the command line. You can use it to store and retrieve snippets of text, and adding tags.

This was realized as a project for the course "Software Engineering" at Dundalk Institute of Technology.
## Usage

Download the jar from [here](https://github.com/sinux-l5d/snipit/releases).

On Windows : `java -jar snipit.jar`

On Linux, you can rename the jar file to `snipit` and run it as a command.

## About the patterns I choose

### Command pattern (used)

This pattern's purpose is generally to delegate logic to a separate class.
In this case, the command pattern is used to delegate the logic of the commands to separate classes.
This makes the code more readable and easier to maintain because each class/command is responsible for a single task.

In SnipIt, the pattern is used through PicoCLI, which uses Callable/Runnable interface for subcommands.
The `CommandLine` class takes a class as a parameter, and go down the class hierarchy to find the `@Command` annotation.

Once initialized, we call the `execute()` method which takes the command line arguments as a parameter, and execute the right command based on them.

![Command pattern UML diagram in SnipIt](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/sinux-l5d/snipit/main/UML/command.puml&fmt=svg)

You can see where `CommandLine` is used in the [Main class line 18](src/main/java/sh/sinux/Main.java#L18).
As an example of a subcommand, you can see the [AddCommand class](src/main/java/sh/sinux/command/AddCommand.java#L22) (see annotation line 22).

This pattern keeps the code clean and every subcommand is responsible for a single task, making it also easier to debug if a subcommand is not working as expected.

### Singleton pattern (implemented)

This pattern's purpose is to ensure that only one instance of a class is created. It's really helpful for object that should be unique, like a database connection or more generally, a single source of truth.

In SnipIt, it's used for both the `Config` object and the `RepositoryProxy` object. In my case, it doesn't make sense to have multiple configuration nor multiple repositories, as I'm pulling data from a single source, and the configuration won't change during runtime.

![Singleton pattern UML diagram in SnipIt](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/sinux-l5d/snipit/main/UML/singleton.puml&fmt=svg)

For implementations, see the [Config class](src/main/java/sh/sinux/config/Config.java#L30) (constructors from line 30) and the [RepositoryProxy class](src/main/java/sh/sinux/repository/RepositoryProxy.java#L33) (constructors from line 33).

Apart from the fact that it's an optimization (only one object) it's also really easy to access the instance, as you can just access it through the `getInstance()` *static* method.
But it comes with a cost, because code using `getInstance()` becomes tightly coupled to the singleton class which can make it harder to replace later.
Because of this issue, no class should access the singleton directly, but instead, it should be passed as a parameter to the constructor. For the subcommands, it calls the `repository()` method of the `Main` class, which returns a `Repository` (and not a `RepositoryProxy`).

### Proxy pattern (implemented)

This pattern's purpose is to provide a placeholder for another object to control access to it. It's really useful when you want to add some logic before or after the execution of a method.

In the app, the RepositoryProxy calls the inner repository to execute the method, and sometimes try to perform some optimizations.
The inner repository used depends on the configuration used when the singleton is initialized.

![Proxy pattern UML diagram in SnipIt](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/sinux-l5d/snipit/main/UML/proxy.puml&fmt=svg)

Implemented in the [RepositoryProxy class](src/main/java/sh/sinux/repository/RepositoryProxy.java).

I've used this pattern to try to optimize operations list `get()`, `save()`, `remove()` and `listNames()`. Because the only implemented storage is filesystem, it's probably not worth it. Nevertheless, if I had to implement a database, it would be useful to cache the snippets name in memory.
So if I implement another storage with higher latency, I can just add a new repository and the proxy will automatically perform optimizations.

However, when no optimizations are needed, the proxy is just a wrapper around the inner repository, so it's redundant.

### DAO pattern (implemented)

By using the DAO pattern, the application's business logic is decoupled from the data access and manipulation logic, making it easier to change the underlying data storage technology without affecting the rest of the application.

Implemented via the [Repository interface](src/main/java/sh/sinux/repository/Repository.java) for dealing with [Snippet](src/main/java/sh/sinux/Snippet.java) objects.
An implementation of this interface is the [FilesystemRepository](src/main/java/sh/sinux/repository/FilesystemRepository.java) class.

![DAO pattern UML diagram in SnipIt](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/sinux-l5d/snipit/main/UML/dao.puml&fmt=svg)

I've used this pattern to completely split the job between data processing and data storing. That allows to change the underlying storage without consequences for the whole app, because it relies on an interface and on an object returned (Repository and Snippet, see UML diagram above).
But if the interface changes, or the record Snippet, all implementation/usage have to be changed.

## Regarding software engineering principles

To make this piece of software, I've tried to respect a few principles, like *separation of concerns*, *anticipation of change* and *abstraction*.

Subcommands and repository for example are loosely coupled. Each command is calling the parent's [`repository()`](src/main/java/sh/sinux/Main.java#L75) method (in Main) to get an object implementing Repository. This leads to abstraction, because a command don't know if it's a `RepositoryProxy` or a `FilesystemRepository`, and it doesn't really matter as long as it does its job.

A Repository is likely to need a configuration object, but it's not creating one itself to follow the same principle.

Anticipation of change have been considered since the beginning with the StorageType enum and the Config class. All it would take to implement a database is writing a class implementing Repository, adding an enumerate variant in StorageType, and add one line to RepositoryProxy which creates a concrete Repository based on to enumerate.
