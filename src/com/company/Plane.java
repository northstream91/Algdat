package com.company;

/**
 * Created by krist on 15-Jan-16.
 */
public class Plane {
    public static int nextAvailableId;
    public int Id;
    public int BirthTime;

    public Plane(int birthTime){
        Id = nextAvailableId;
        nextAvailableId++;
        BirthTime = birthTime;
    }
}
