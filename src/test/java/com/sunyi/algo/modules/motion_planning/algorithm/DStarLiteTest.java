package com.sunyi.algo.modules.motion_planning.algorithm;


import org.junit.jupiter.api.Test;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class DStarLiteTest {


    @Test
    void searchAlgorithmsTest() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long startTime = threadMXBean.getCurrentThreadCpuTime();
        // 要测试的代码块

        long endTime = threadMXBean.getCurrentThreadCpuTime();
        long elapsedTime = endTime - startTime;
        System.out.println("耗时: " + elapsedTime + " 纳秒");

    }
}