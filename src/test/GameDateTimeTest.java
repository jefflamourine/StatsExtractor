package test;

import main.core.GameIdentity;

import org.junit.Test;

public class GameDateTimeTest {

    @Test
    public void testConstructionAndToString() {
        GameIdentity g = new GameIdentity("a", "b", "031516");
        System.out.println(g.date);
    }
}
