package de.niekrenz.intelhex.analyze.memmodel;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.Set;
import java.util.regex.Pattern;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import static java.lang.String.format;
import static java.util.stream.Collectors.toSet;

public class MemoryModelResourceFinder {

    private static final String PACKAGE_NAME = "de.niekrenz.intelhex";
    private static final String BUILD_PATH_GRADLE = "build/resources/main";
    private static final String BUILD_PATH_INTELLIJ = "out/production/resources"; // local unit test in IntelliJ

    private Reflections reflections;
    private final ClassLoader classLoader;
    private final MemoryModelParser memoryModelParser;

    public MemoryModelResourceFinder() {
        initReflections();
        classLoader = getClass().getClassLoader();
        memoryModelParser = new MemoryModelParser();
    }

    public MemoryModel open(String name) {
        return readAllModels().stream()
                .filter(model -> model.getAliases().contains(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        format("Unknown memory model '%s'", name)));
    }

    private Set<MemoryModel> readAllModels() {
        return reflections.getResources(Pattern.compile(".*\\.yml")).stream()
                .filter(fileName -> fileName.startsWith("memory-model"))
                .map(classLoader::getResourceAsStream)
                .map(memoryModelParser::parse)
                .collect(toSet());
    }

    private void initReflections() {
        try {
            reflections = new Reflections(new ConfigurationBuilder()
                    .addUrls(ClasspathHelper.forPackage(PACKAGE_NAME))
                    .addUrls(Paths.get(BUILD_PATH_GRADLE).toUri().toURL())
                    .addUrls(Paths.get(BUILD_PATH_INTELLIJ).toUri().toURL())
                    .setScanners(new ResourcesScanner()));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
