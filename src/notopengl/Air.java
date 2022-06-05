package notopengl;

import java.awt.Color;

public class Air extends Gaz {

    protected Air() {
        super("air", 0.0f, 1, new Color(0, 0, 0, 0));
    }

    @Override
    public void update(Cell c) {
        return;
    }

    @Override
    public World.Material combustion() {
        return null;
    }
}
