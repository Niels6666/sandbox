package notopengl;
import java.awt.Color;

public class Wood extends Substance {

    protected Wood() {
        super("wood", Float.POSITIVE_INFINITY, new Color(90, 50, 1), 0.006f, 1);
    }

    @Override
    public void update(Cell c) {
        return;
    }

    @Override
    public World.Material combustion() {
        return World.Material.BURNINGWOOD;
    }
    
}
