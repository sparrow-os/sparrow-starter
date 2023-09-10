package com.sparrow.spring.test;

import net.agkn.hll.HLL;
import net.agkn.hll.HLLType;
import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.longlong.Roaring64Bitmap;

import java.util.Random;

public class HllTest {
    /**
     * 翻译
     *
     * @param log2m     桶数的log-base-2 比如桶数为64, 则log2m=6,该参数为4到30之间
     * @param regwidth  每个桶的位数，该参数在1到8之间,即最大为一个字节
     * @param expthresh 当EXPLICIT提升为SPARSE的阈值，取值范围为1-18
     *                  expthresh	含义
     *                  -1	Promote at whatever cutoff makes sense for optimal memory usage.
     *                  0	Skip EXPLICIT representation in hierarchy
     *                  1-18	Promote at 2expthresh - 1 cardinality
     *                  表格中的参数含义不直接翻译，直接看代码
     */
    public static void main(String[] args) {
        HLL hll = new HLL(8, 8, 8, true, HLLType.EMPTY);
        Roaring64Bitmap rbm = new Roaring64Bitmap();

        for (long i = 0; i < 1000000L; i++) {
            hll.addRaw(i);
            hll.addRaw(i);
            rbm.add(i);
            rbm.add(i);
        }
        System.out.println(hll.cardinality());
        System.out.println(rbm.getLongCardinality());


    }
}
