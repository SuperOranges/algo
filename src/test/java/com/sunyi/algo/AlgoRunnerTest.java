package com.sunyi.algo;

import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AlgoRunnerTest {

    int[][] x = new int[50][50];

    ReadWriteLock rwLock = new ReentrantReadWriteLock();

    AtomicIntegerArray[] array = new AtomicIntegerArray[50];

    {
        for (int i = 0; i < 50; i++) {
            AtomicIntegerArray integerArray = new AtomicIntegerArray(50);
            array[i] = integerArray;
        }

    }

    Thread thread = new Thread(new Runnable() {
        @SneakyThrows
        @Override
        public void run() {
            Thread.sleep(20);
//            for (int i = 0; i < 50 ; i++) {
//                for (int j = 0; j < 50 ; j++) {
//                    x[i][j] = 1;
//                }
//            }
            Lock writeLock = rwLock.writeLock();
            writeLock.lock();
            System.out.println("Thread time" + System.currentTimeMillis());
            for (int i = 0; i < 50; i++) {
                for (int j = 0; j < 50; j++) {
                    array[i].set(j, 1);
                }
            }
            System.out.println("Thread time" + System.currentTimeMillis());
            writeLock.unlock();
        }
    });

    @Test
    public void getSolution() throws InterruptedException {

        thread.start();
        Thread.sleep(21);
        System.out.println("main time" + System.currentTimeMillis());
//        for (int i = 0; i < 50 ; i++) {
//            for (int j = 0; j < 50 ; j++) {
//                x[i][j] = 0;
//            }
//        }
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                array[i].set(j, 2);
            }
        }
        System.out.println("main time" + System.currentTimeMillis());
//        for (int[] row: x) {
//            System.out.print("[");
//            for (int i = 0; i < row.length; i++) {
//                System.out.print(row[i]);
//                if (i < row.length - 1) {
//                    System.out.print(", ");
//                }
//            }
//            System.out.println("]");
//        }

        for (AtomicIntegerArray row : array) {
            System.out.print("[");
            for (int i = 0; i < row.length(); i++) {
                System.out.print(row.get(i));
                if (i < row.length() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }
    }
}