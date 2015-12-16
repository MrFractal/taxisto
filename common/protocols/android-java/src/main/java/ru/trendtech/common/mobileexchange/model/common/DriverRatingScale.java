package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 08.08.14.
 */
public class DriverRatingScale {
//    0-5 None
//    5-6 Green
//    6-7 Silver
//    7-8 Gold
//    8-9 Black
//    9-10 Brilliant

    /*
     0-5 просто пишется "рейтинг такой-то"
     5-7 Silver
     7-8 Gold
     8-9 Platinum
     9-10 Brilliant
     */


    public String getNameRating(double rating){
          if(rating>0&&rating<=5){
              return "";
          }else if(rating>5&&rating<=7){
              return "silver";
          } else if(rating>7&&rating<=8){
              return "gold";
          }else if(rating>8&&rating<=9){
              return "platinum";
          }else if(rating>9&&rating<=10){
              return "brilliant";
          }else{
              return "undefined";
          }
    }

//
//    public static boolean intervallContains(int low, int high, int n) {
//        return n >= low && n <= high;
//    }


}
