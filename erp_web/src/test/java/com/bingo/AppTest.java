package com.bingo;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    public static double average(double a, double b) {
        return a + b / 2;
    }

    public static void main(String[] args) {
        System.out.println(average(2,1));
    }
}
