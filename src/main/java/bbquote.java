///usr/bin/env jbang "$0" "$@" ; exit $?

//DEPS info.picocli:picocli:4.6.3
//DEPS com.konghq:unirest-java:3.14.1
//DEPS com.konghq:unirest-object-mappers-gson:3.14.1
//SOURCES models

import kong.unirest.GenericType;
import kong.unirest.Unirest;
import models.Quote;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Command(name = "bbquote", mixinStandardHelpOptions = true, version = "BreakingBadCli 0.1",
        description = "BreakingBadCli made with jbang")
class bbquote implements Callable<Integer> {

    @Parameters(index = "0", description = "input file with characters")
    private Path path;

    public static void main(String... args) {
        int exitCode = new CommandLine(new bbquote()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        var characters = Files.readAllLines(path);
        characters.forEach(character -> {
            var res = Unirest.get("https://api.breakingbadquotes.xyz/v1/quotes/50")
                    .asObject(new GenericType<List<Quote>>() {
                    })
                    .getBody();
            var filteredQuote = res.stream()
                    .filter(item -> item.getAuthor().contains(character))
                    .collect(Collectors.toList());
            if (filteredQuote.size() > 0) {
                System.out.println(character + " once says...");
                filteredQuote.forEach(quote -> System.out.println("..." + quote.getQuote()));
            }
        });
        return 0;
    }
}
