package ru.trendtech.utils.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by petr on 05.11.2015.
 */
public class App01 {

    public static void main(String[] args) {
        List<String> stringList = new ArrayList<>();
        stringList.add("aa");
        stringList.add("bb");
        stringList.add("cc");
        System.out.println(stringList);
        for(Iterator<String> iterator = stringList.iterator(); iterator.hasNext();){
               if(iterator.next().equals("aa")){
                    iterator.remove();
               }
        }
        System.out.println("-----");
        System.out.println(stringList);

        System.out.println("-----");


        List<Integer> holdList = Arrays.asList(100, 200, 300);
        System.out.println(holdList);

        System.out.println();


        List<Integer> newList = new ArrayList<>(holdList);
        newList.add(500);
        System.out.println(newList);

    }
}
