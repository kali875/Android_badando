package com.example.beadando;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class User
{
    static public int ID;
    static public String Name,PhoneNumber,Email;
    static public String target;
    Location currentLocation;

    static List<String> search_friends = new ArrayList();

    static List<String> friends = new ArrayList();

    public User(int ID)
    {
        this.ID = ID;
    }

    static LatLng select_friend_pos;

    public static int get_ID()
    {
        return ID;
    }
    public static void set_ID(int value)
    {
        ID= value;
    }
    public Location get_currentLocation()
    {
        return this.currentLocation;
    }
    public void set_currentLocation(Location value)
    {
        this.currentLocation = value;
    }
}
