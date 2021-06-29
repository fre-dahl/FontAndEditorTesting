package graphics.test;


import graphics.test.text.TextEditor;

import java.util.Arrays;

public class test {

    public static void main(String[] args) {


        TextEditor editor = new TextEditor();

        editor.insert("Hello World!");

        editor.moveBackward();
        editor.delete();
        editor.delete();
        editor.delete();
        editor.delete();
        editor.delete();

        editor.clear();

        editor.insert("Fuckface");

        editor.moveToStart();

        editor.space();
        editor.space();
        editor.moveToEnd();
        editor.space();
        editor.insert("Frankenstein");

        editor.moveToStart();

        editor.insert("sdsdd");
        editor.newLine();
        editor.tab();



        System.out.println(editor);

        System.out.println(Arrays.toString(editor.getBytes()));


    }
}
