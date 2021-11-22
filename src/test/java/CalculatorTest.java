import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {

   public static Calculator calculator;

    @BeforeAll
    public static void setup() {
        calculator = new Calculator();
    }

    @Test
    public void add_success() {
        long a = calculator.add(2L,3L);
        assertEquals(5,a);
    }
}
