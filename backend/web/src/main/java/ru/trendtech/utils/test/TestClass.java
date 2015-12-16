package ru.trendtech.utils.test;

import java.util.Arrays;
import java.util.List;
//import java.util.stream.Collectors;

public class TestClass {
        public static void main(String[] args) {
                Student s1 = new Student("Ram", 18);
                Student s2 = new Student("Shyam",22);
                Student s3 = new Student("Mohan",19);
                Student s4 = new Student("Mahesh",20);
                Student s5 = new Student("Krishna",21);
                List<Student> list = Arrays.asList(s1, s2, s3, s4, s5);


                /*

                //partition of Student on the basis of age
                System.out.println("----Partition of Student on the basis of age >20 ----");
                Map<Boolean, List<Student>> stdByClass = list.stream()
                        .collect(Collectors.partitioningBy(s -> s.getAge() > 20));

                stdByClass.forEach((k,v)->System.out.println("Key:"+k+"  "+
                        ((List<Student>)v).stream().map(s -> s.getName()).collect(Collectors.joining(","))));

                */


        }
    }




