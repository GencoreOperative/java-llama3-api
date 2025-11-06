package uk.co.gencoreoperative.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ResourceResolverTest {

    @Test(expected = NullPointerException.class)
    public void resolveWithNullResourceNameThrowsNullPointerException() {
        ResourceResolver.resolve(null);
    }

    @Test
    public void resolveCopiesBadgerResourceToTempDirectory() throws IOException {
        Path resolved = ResourceResolver.resolve("/badger");

        assertThat(resolved).exists();
        assertThat(Files.readString(resolved).trim()).isEqualTo("badgers are the best");
    }
}