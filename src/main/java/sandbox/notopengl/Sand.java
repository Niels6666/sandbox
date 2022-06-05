package sandbox.notopengl;
import java.awt.Color;

public class Sand extends Substance {

    protected Sand() {
        super("sand", 1.5f, new Color(250, 250, 50), 0f, 1);
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
