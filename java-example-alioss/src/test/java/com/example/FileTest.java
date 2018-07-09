package com.example;

import java.io.Writer;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class FileTest {

    public static void main(String[] args) throws Exception {
        String fileName = args[0];
        String packageId = args[1];

        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        URI uri = URI.create(fileName);
        try (java.nio.file.FileSystem fs = FileSystems.newFileSystem(uri, env)) {
            Path nf = fs.getPath("META-INF\\config.txt");
            try (Writer writer = Files.newBufferedWriter(nf, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
                writer.write(String.format("id=%s", packageId));
            }
        }
    }
}
