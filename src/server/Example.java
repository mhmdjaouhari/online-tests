package server;

import java.util.ArrayList;

// equivalent to DAO
// this is an example class for the other ones to be implemented (Student, Test...)
// these classes will contain the logic that will extract the data from the database and prepare it to be sent
public class Example {
    public static ArrayList<String> getExamples(){
        ArrayList<String> list = new ArrayList<String>();
        list.add("hello");
        list.add("hi");
        list.add("another example");
        list.add("these are all just examples!");
        return list;
    }
}
