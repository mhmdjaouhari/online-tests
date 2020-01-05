package client;

import java.util.ArrayList;

// this is an example class for the other APIs to be implemented (Student, Test...)
// these classes will contain the logic that will interact with the server via Sockets
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
