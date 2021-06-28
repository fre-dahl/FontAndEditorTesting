import org.joml.Vector2f;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        //File fontFolder = new File("fonts/ttf");
        //String outFolder = "fonts/png/";

        //Font font = new Font("fonts/ttf/Topaz_a500_v1.0.ttf", outFolder + "Topaz_a500_16",16);

        Window window = new Window();
        window.run();



        /*
        File[] ttfs = fontFolder.listFiles();

        for (int i = 0; i < ttfs.length; i++) {
            System.out.println(ttfs[i].getPath());
            font = new Font(ttfs[i].getPath(), outFolder + "font" + i,64);
        }

         */
    }
}
