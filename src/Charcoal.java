import java.awt.Color;
import java.util.Random;

public class Charcoal extends Substance {
    protected Charcoal() {
        super("charcoal", Float.POSITIVE_INFINITY, new Color(20, 10, 8), 0.005f);
    }

    @Override
    public void update(Cell c, World w) {
        // Cell above = w.above(c);
        // Cell below = w.below(c);
        // Cell below_left = w.left(below);
        // Cell below_right = w.right(below);

        // if (chanceToRise(c, above)) {
        //     swapSubstances(c, above);
        //     return;
        // }
        
        // if (isEmpty(below_left)) {
        //     swapSubstances(below_left, c);
        //     return;
        // }

        // if (isEmpty(below_right)) {
        //     swapSubstances(below_right, c);
        // }
    }

    @Override
    public World.Material combustion() {
        Random r = new Random();
        if (r.nextInt(100) > 99) {
            return World.Material.FIRE;
        } else {
            return World.Material.SMOKE;
        }
    }
}
