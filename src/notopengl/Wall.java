package notopengl;
import java.awt.Color;

public class Wall extends Substance {

    protected Wall() {
        super("wall", Float.POSITIVE_INFINITY, new Color(255, 255, 255), 0f, 1);
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
