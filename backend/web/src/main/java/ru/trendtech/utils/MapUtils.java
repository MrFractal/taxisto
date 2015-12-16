package ru.trendtech.utils;

import java.util.*;

/**
 * Created by petr on 16.06.2015.
 */
public class MapUtils {
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue2( Map<K, V> map ) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }




    public static Map sortByValue(HashMap unsortedMap){
        Map sortedMap = new TreeMap(new ValueComparator(unsortedMap));
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }



    public static Map sortByKey(Map unsortedMap){
        Map sortedMap = new TreeMap<String, Object>(
                new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o2.compareTo(o1);
                    }

                });
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }




    public static class ValueComparator implements Comparator {
        HashMap map;

        public ValueComparator(HashMap map){
            this.map = map;
        }
        public int compare(Object keyA, Object keyB){

            Comparable valueA = (Comparable) map.get(keyA);
            Comparable valueB = (Comparable) map.get(keyB);

            System.out.println(valueA +" - "+valueB);

            return valueA.compareTo(valueB);

        }
    }
}