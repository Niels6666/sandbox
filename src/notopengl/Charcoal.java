package notopengl;
import java.awt.Color;
import java.util.Random;

public class Charcoal extends Substance {
    protected Charcoal() {
        super("charcoal", Float.POSITIVE_INFINITY, new Color(20, 10, 8), 1.0f, 1);
    }

    @Override
    public void update(Cell c) {
        World w = c.w;
        Cell below = w.below(c);
        Cell below_left = w.left(below);
        Cell below_right = w.right(below);

        if (canMove(c, below)) {
            swapSubstances(below, c);
            return;
        }

        if (canMove(c, below_left)) {
            swapSubstances(below_left, c);
            return;
        }

        if (canMove(c, below_right)) {
            swapSubstances(below_right, c);
            return;
        }
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
