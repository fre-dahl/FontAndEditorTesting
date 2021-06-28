package graphics.test;


import graphics.test.text.TextEditor;

public class test {

    public static void main(String[] args) {



        TextEditor editor = new TextEditor(200,8,200,true);

        editor.commandInsert("@davidmoten needed to render text for display in a PDF using PDFBox but PDFBox didn't offer word wrapping. He searched for libraries to do it and found Apache commons-text and commons-lang WordUtils but it didn't conserve leading spaces on lines and didn't allow for a customizable string width function. With a bit of luck this library will help those who are on a similar search!");

        //editor.print();
        editor.getBytes();





    }
}
