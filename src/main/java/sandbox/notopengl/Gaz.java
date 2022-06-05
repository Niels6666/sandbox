package sandbox.notopengl;
import java.awt.Color;

public abstract class Gaz extends Substance{
    protected Gaz(String name, float mass, long maxLife, Color color) {
        super(name, mass, color, 0, maxLife);
    }

    @Override
    public World.Material combustion() {
        return null;
    }
}
