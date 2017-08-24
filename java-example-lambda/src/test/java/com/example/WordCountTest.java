package com.example;

import org.junit.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class WordCountTest {

    @Test
    public void test() throws Exception {
        Path path = getPath("words.txt");
        Map<String, Long> wordCount = Files.lines(path)
                .parallel()
                .flatMap(line -> Arrays.stream(line.trim().split("\\s")))
                .map(word -> word.replaceAll("[^a-zA-Z]", "").toLowerCase().trim())
                .filter(word -> word.length() > 0)
                .map(word -> new AbstractMap.SimpleEntry<>(word, 1))
                .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey, Collectors.counting()));
        wordCount.forEach((k, v) -> System.out.println(String.format("%s ==>> %d", k, v)));
    }

    private Path getPath(String classpath) {
        URL file = getClass().getClassLoader().getResource(classpath);
        if (file != null) {
            String path = file.getPath();
            if (path.startsWith("/"))
                path = path.substring(1);
            return Paths.get(path);
        }
        return null;
    }
}
