import java.awt.Color;

public abstract class Liquid extends Substance {
    protected int spreadFactor;
    protected Liquid(String name, float mass, Color color, float igniteCoeff, int spreadFactor) {
        super(name, mass, color, igniteCoeff);
        this.spreadFactor = spreadFactor;
    }

}
