package com.sya.classeats;

public class Group {
    int conencted;
    int wanna_Eat;
    String group_Name;

    public Group(String name, int connected, int eat) {
        this.conencted = connected;
        this.group_Name = name;
        this.wanna_Eat = eat;
    }
    public Group(String name) {
        this.group_Name = name;
    }

    public String getGroup_Name() {
        return group_Name;
    }

    public void setGroup_Name(String s) {
        group_Name = s;
    }

    public int getConencted() {
        return conencted;
    }

    public void setConnected(int i) {
        conencted = i;
    }

    public int getWanna_Eat() {
        return wanna_Eat;
    }

    public void setWanna_Eat(int i) {
        wanna_Eat = i;
    }

    @Override
    public String toString() {
        return "Group " + group_Name + " " + conencted + "connected people" + " " + wanna_Eat + "wants to eat";
    }

}
