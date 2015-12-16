package ru.trendtech.common.mobileexchange.model.common;

/**
 * Created by petr on 07.11.2014.
 */


public class CountClientHelper{
    private int countClientApple=0;
    private int countClientAndroid=0;
    private int countClientTerminal=0;
    private int countClientOther=0;

    public int getCountClientOther() {
        return countClientOther;
    }

    public void setCountClientOther(int countClientOther) {
        this.countClientOther = countClientOther;
    }

    public int getCountClientTerminal() {
        return countClientTerminal;
    }

    public void setCountClientTerminal(int countClientTerminal) {
        this.countClientTerminal = countClientTerminal;
    }

    public int getCountClientApple() {
        return countClientApple;
    }

    public void setCountClientApple(int countClientApple) {
        this.countClientApple = countClientApple;
    }

    public int getCountClientAndroid() {
        return countClientAndroid;
    }

    public void setCountClientAndroid(int countClientAndroid) {
        this.countClientAndroid = countClientAndroid;
    }
}
