import java.awt.Color;

public class Wood extends Substance {

    protected Wood() {
        super("wood", Float.POSITIVE_INFINITY, new Color(110, 70, 10), 0.006f);
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
