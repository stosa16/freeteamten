package at.sw2017.pocketdiary;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PinScreenUnitTest {

    @Test
    public void is_Pin_correct() throws Exception {
        boolean pin_cor;
        pin_cor = PinCheck.check_PIN("a", "a");
        assertEquals(true, pin_cor);
        pin_cor = PinCheck.check_PIN("b", "a");
        assertEquals(false, pin_cor);
        }
}
