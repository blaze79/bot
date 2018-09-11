package org.silentpom.runner.domain.masks;

import org.silentpom.runner.domain.Position;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by Vlad on 10.09.2018.
 */
public class BitMaskTest {
    @Test
    public void testPrint() throws Exception {
        BitMask bm = new BitMask(3,4);
        bm.setChecked(new Position(0, 0), true);
        bm.setChecked(new Position(1, 1), true);
        bm.setChecked(new Position(2, 2), true);
        bm.setChecked(new Position(2, 3), true);

        bm.print();
    }

}