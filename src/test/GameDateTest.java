package test;

import static org.junit.Assert.assertEquals;
import main.core.GameDate;

import org.junit.Test;

public class GameDateTest {

    @Test
    public void testConstructionAndToString() {
        GameDate date = new GameDate(2016, 1, 21, 19, 30);
        assertEquals(date.toString(), "Thu January 21 2016 19:30:00");
    }
    
}
