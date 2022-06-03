import java.awt.Color;

public class Ash extends Substance {

    protected Ash() {
        super("ash", 0.7f, new Color(40, 40, 40), 0f);
    }

    @Override
    public void update(Cell c, Grid g) {
        Cell above = c.above();
        Cell below = c.below();
        Cell below_left = below.left();
        Cell below_right = below.right();

        if (chanceToRise(c, above)) {
            swapSubstances(c, above, g);
            return;
        }

        if (isEmpty(below_left)) {
            swapSubstances(below_left, c, g);
            return;
        }

        if (isEmpty(below_right)) {
            swapSubstances(below_right, c, g);
        }
    }

    @Override
    public World.Material combustion() {
        return null;
    }

}
