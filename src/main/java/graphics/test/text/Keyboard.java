package graphics.test.text;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * @author Frederik Dahl
 * 29/06/2021
 */

public class Keyboard {

    private static Keyboard instance;

    private final boolean[] keys = new boolean[GLFW_KEY_LAST];

    private CharRegister reader;

    private Keyboard() {}


    public static Keyboard get() {

        if (instance == null)

            instance = new Keyboard();

        return instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {

        if (key == GLFW_KEY_UNKNOWN) return;

        if (action == GLFW_PRESS)

            get().keys[key] = true;

        else if (action == GLFW_RELEASE)

            get().keys[key] = false;
    }

    public static void charCallback(long window, int codepoint) {

        CharRegister reader = get().reader;

        if (reader != null) {

            if (reader.isSleeping()) return;

            if ((codepoint & 0x7F) == codepoint)

                reader.registerInput((byte)codepoint);
        }
    }

    public static boolean isPressed(int keyCode) {

        Keyboard keyboard = Keyboard.get();

        if (keyCode >= keyboard.keys.length) return false;

        return keyboard.keys[keyCode];
    }

    public void setReader(CharRegister reader) {

        boolean thisReaderNull = this.reader == null;

        boolean newReaderNull = reader == null;

        if (thisReaderNull) {

            if (newReaderNull) return;

            this.reader = reader;

            reader.signalActivate();
        }
        else {

            if (newReaderNull) {

                this.reader.signalDeactivate();

                this.reader = null;
            }
            else {

                if (this.reader.equals(reader)) return;

                this.reader.signalDeactivate();

                this.reader = reader;

                reader.signalActivate();
            }
        }
    }
}
