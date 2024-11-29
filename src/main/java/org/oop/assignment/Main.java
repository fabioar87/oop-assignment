package org.oop.assignment;

// Research
// socket programming:
//      1. client-side

public class Main {
    public static void main(String[] args) {

        DictClient client = new DictClient();
        String word = "gold";
        String dictionary = "fd-eng-lat";

        client.callTranslation(word, dictionary);
    }
}
