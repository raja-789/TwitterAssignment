package com.twitter.challenge;

import static com.twitter.challenge.TemperatureConverter.celsiusToFahrenheit;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TemperatureConverterTests {
    @Test
    public void testCelsiusToFahrenheitConversion() {
        assertTrue("Result", celsiusToFahrenheit(-50) == -58);
        assertTrue("Result", celsiusToFahrenheit(21.11f) == 70);
    }
}
