package uk.co.gencoreoperative.utils;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.forgerock.cuppa.Cuppa.beforeEach;
import static org.forgerock.cuppa.Cuppa.describe;
import static org.forgerock.cuppa.Cuppa.it;
import static org.forgerock.cuppa.Cuppa.when;

import org.forgerock.cuppa.junit.CuppaRunner;
import org.junit.runner.RunWith;

@RunWith(CuppaRunner.class)
public class StdOutUtilsTest {

    private Runnable function;

    {
        describe(StdOutUtils.class.getSimpleName(), () -> {
            when("outputting to STD", () -> {
                beforeEach(() -> {
                    function = () -> {
                        System.out.println("badger");
                    };
                });
                it("it is returned", () -> {
                    String response = StdOutUtils.executeWithRedirect(function).out();
                    assertThat(response).isNotEmpty();
                });
            });
            when("outputting to STDERR", () -> {
                beforeEach(() -> {
                    function = () -> {
                        System.err.println("badger");
                    };
                });
                it("it returns nothing", () -> {
                    String response = StdOutUtils.executeWithRedirect(function).out();
                    assertThat(response).isEmpty();
                });
            });
        });
    }
}