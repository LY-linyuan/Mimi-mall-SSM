package com.bjpowernode.test;

import com.bjpowernode.utils.MD5Util;
import org.junit.Test;

/**
 * @Author 临渊
 * @Date 2022-08-04 16:18
 */
public class MyTest {
    @Test
    public void testMD5() {
        String mi = MD5Util.getMD5("000000");
        System.out.println(mi);
    }
}
