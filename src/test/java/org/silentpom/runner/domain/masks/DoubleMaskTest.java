package org.silentpom.runner.domain.masks;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by Vlad on 10.09.2018.
 */
public class DoubleMaskTest {
    @Test
    public void testPrint() throws Exception {
        DoubleMask dm = new DoubleMask(3, 3);
        dm.setValue(0, 0, 1.1);
        dm.setValue(1, 1, 22.2);
        dm.setValue(2, 2, 333.3);
        dm.print();
    }

}