import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {

    Calculator calculator = new Calculator();

    @Test
    public void add_success() {
        long a = calculator.add(2L,3L);
        assertEquals(5,a);
    }
}
