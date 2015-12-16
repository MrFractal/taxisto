package ru.trendtech.domain;

/**
 * File created by max on 08/05/2014 14:28.
 */

public enum MissionService {
    UNKNOWN(0),
    CHILDREN(1),
    BAGGAGE(2),
    ANIMAL(3),
    MEETING_WITH_SIGN(4),
    SMALL_ANIMAL(5),
    CONDITIONER(6),
    SMALL_CHILDREN(7),
    ;


    private int value;

    MissionService(int value) {
        this.value = value;
    }

    public static MissionService getByValue(int value) {
        MissionService result = UNKNOWN;
        for (MissionService item : values()) {
            if (item.getValue() == value) {
                result = item;
                break;
            }
        }
        return result;
    }

    public int getValue() {
        return value;
    }

    public static int getValueStr(MissionService str){
        int t = 0;

        switch (str){
            case CHILDREN:{
                t = 1;
                break;
            }
            case BAGGAGE:{
                t = 2;
                break;
            }
            case ANIMAL:{
                t = 3;
                break;
            }
            case MEETING_WITH_SIGN:{
                t = 4;
                break;
            }
            case SMALL_ANIMAL:{
                t = 5;
                break;
            }
            case CONDITIONER:{
                t = 6;
                break;
            }
            case SMALL_CHILDREN:{
                t = 7;
                break;
            }
        }
        return t;
    }
}
