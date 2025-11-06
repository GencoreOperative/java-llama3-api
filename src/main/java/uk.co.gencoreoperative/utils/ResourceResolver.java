package uk.co.gencoreoperative.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import lombok.NonNull;

/**
 * Utility methods for resolving bundled resources to a usable Path.
 */
public class ResourceResolver {
    public static final File TMP = new File(System.getProperty("java.io.tmpdir"));

    private ResourceResolver() {
        // Utility class
    }

    /**
     * Given a classpath resource, convert it into a file on the file system.
     * <p>
     * This function will extract the resource to a temporary file location if it has not been extracted already.
     * Subsequent calls with the same resource name will return the same file path.
     *
     * @param name Non-null name of the classpath resource to resolve.
     * @return Path to the resolved resource on the file system.
     */
    public static Path resolve(@NonNull String name) {
        try (InputStream resource = ResourceResolver.class.getResourceAsStream(name)) {
            if (resource == null) throw new IllegalArgumentException("Resource not found: " + name);
            File resourceFile = new File(TMP, Integer.toString(name.hashCode()));
            if (!resourceFile.exists()) {
                FileOutputStream stream = new FileOutputStream(resourceFile);
                resource.transferTo(stream);
                stream.flush();
                stream.close();
            }
            return resourceFile.toPath();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load the resource: " + name, e);
        }
    }
}
