package org.silentpom.runner.domain.masks;

import org.silentpom.runner.domain.state.PositionsCache;
import org.testng.annotations.Test;

/**
 * Created by Vlad on 10.09.2018.
 */
public class BitMaskTest {
    @Test
    public void testPrint() throws Exception {
        BitMask bm = new BitMask(3,4);
        bm.setChecked(PositionsCache.make(0, 0), true);
        bm.setChecked(PositionsCache.make(1, 1), true);
        bm.setChecked(PositionsCache.make(2, 2), true);
        bm.setChecked(PositionsCache.make(2, 3), true);

        bm.print();
    }

}