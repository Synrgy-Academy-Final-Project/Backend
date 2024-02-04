package com.example.finalProject;

import java.util.ArrayList;

class playground {

    // Main driver method
    public static void main(String[] args)
    {

        // Creating an empty ArrayList of string type
        ArrayList<String> al = new ArrayList<String>();

        // Populating the ArrayList by custom elements
        al.add("Anshul Aggarwal");
        al.add("Mayank Solanki");
        al.add("Abhishek Kelenia");
        al.add("Vivek Gupta");

        // Iterating over above string array
        System.out.println(String.join(" ", al.subList(1, al.size())));
    }
}