import java.awt.Color;

public class Air extends Gaz {

    protected Air() {
        super("air", 0.0f, new Color(0,0,0,0));
        igniteCoeff = 0.01f;
    }

    @Override
    public void update(Cell c) {
        return;
    }

    @Override
    public World.Material combustion() {
        return World.Material.SMOKE;
    }
}
