package com.bingo;

public class SortedSearch {

    public static int countNumbers(int[] sortedArray, int lessThan) {

        int halfLength = sortedArray.length/2;

        int begin = 0;

        int end = sortedArray.length - 1;


        while (!(begin >= halfLength)){

            if (sortedArray[halfLength] <= lessThan){


                begin = halfLength;
                halfLength = (begin + end) / 2;
            }else {
                end = halfLength;
                halfLength = (begin + end) / 2;
            }
        }
        halfLength = halfLength+1;
        return halfLength;


    }

    public static void main(String[] args) {
        System.out.println(SortedSearch.countNumbers(new int[] { 1, 3, 5, 7 }, 4));
    }
}
