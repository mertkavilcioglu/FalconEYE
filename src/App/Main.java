package App;

import App.EYEApp;

import java.lang.reflect.InvocationTargetException;

public class Main{
    public static void main(String[] args) {
        EYEApp app = new EYEApp();
        try {
            app.runWithWindow();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}
