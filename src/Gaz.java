import java.awt.Color;

public abstract class Gaz extends Substance{
    protected Gaz(String name, float mass, Color color) {
        super(name, mass, color, 0);
    }

    @Override
    public World.Material combustion() {
        //gaz dont burn
        return null;
    }
}
