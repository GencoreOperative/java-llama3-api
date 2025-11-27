package uk.co.gencoreoperative.utils;


import org.forgerock.cuppa.junit.CuppaRunner;
import org.junit.runner.RunWith;

import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.forgerock.cuppa.Cuppa.beforeEach;
import static org.forgerock.cuppa.Cuppa.describe;
import static org.forgerock.cuppa.Cuppa.it;
import static org.forgerock.cuppa.Cuppa.when;

@RunWith(CuppaRunner.class)
public class ResourceResolverTest {

    private String path;

    {
        describe("ResourceResolver", () -> {

            when("resource name is null", () -> {
                beforeEach(() -> {
                    path = null;
                });
                it("throws NullPointerException", () -> {
                    Throwable thrown = catchThrowable(() -> ResourceResolver.resolve(path));
                    assertThat(thrown).isInstanceOf(NullPointerException.class);
                });
            });

            when("provided with a valid resource", () -> {
                beforeEach(() -> {
                    path = "/badger";
                });
                it("reads it", () -> {
                    Path resolve = ResourceResolver.resolve(path);
                    assertThat(resolve).exists();
                    assertThat(Files.readString(resolve).trim()).isEqualTo("badgers are the best");
                });
            });
        });
    }
}