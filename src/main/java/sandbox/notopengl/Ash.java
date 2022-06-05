package sandbox.notopengl;
import java.awt.Color;

public class Ash extends Substance {

    protected Ash() {
        super("ash", 0.7f, new Color(40, 40, 40), 0f, 1);
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
        return null;
    }

}
