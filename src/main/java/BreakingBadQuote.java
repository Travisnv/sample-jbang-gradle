///usr/bin/env jbang "$0" "$@" ; exit $?

//DEPS com.konghq:unirest-java:3.14.1
//DEPS com.konghq:unirest-object-mappers-gson:3.14.1
//SOURCES models

import kong.unirest.GenericType;
import kong.unirest.Unirest;
import models.Quote;

import java.util.List;
import java.util.stream.Collectors;

public class BreakingBadQuote {

    public static void main(String... args) {
        var character = args[0];
        var number = Integer.valueOf(args[1]);
        System.out.println("Starting...");
        var res = Unirest.get("https://api.breakingbadquotes.xyz/v1/quotes/" + number)
                .asObject(new GenericType<List<Quote>>() {
                })
                .getBody();
        var hank = res.stream()
                .filter(item -> item.getAuthor().contains(character))
                .map(item -> String.format("%s once says...%s", character, item.getQuote()))
                .collect(Collectors.toList());
        hank.forEach(quote -> System.out.println(quote));
    }

}
