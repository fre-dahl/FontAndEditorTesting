package graphics.test.text;

/**
 * @author Frederik Dahl
 * 29/06/2021
 */

public interface CharRegister {

    void registerInput(byte keyCode);

    void signalActivate();

    void signalDeactivate();

    boolean isSleeping();
}
