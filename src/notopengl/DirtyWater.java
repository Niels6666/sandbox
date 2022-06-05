package notopengl;
import java.awt.Color;
import java.util.Random;

public class DirtyWater extends Substance {
    protected DirtyWater() {
        super("dirty water", 1.2f, new Color(10, 40, 110, 200), 0f, 1);
    }

    @Override
    public void update(Cell c) {
        World w = c.w;
        Cell below = w.below(c);
        Cell below_left = w.left(below);
        Cell below_right = w.right(below);

        Cell below_left2 = w.below(w.left(below_left));
        Cell below_right2 = w.below(w.right(below_right));

        Cell cell_left = w.left(c);
        Cell cell_right = w.right(c);

        if (canMove(c, below)) {
            swapSubstances(c, below);
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

        if (canMove(c, below_left2)) {
            swapSubstances(below_left2, c);
            return;
        }

        if (canMove(c, below_right2)) {
            swapSubstances(below_right2, c);
            return;
        }

        if (chanceToGoLeft()) {
            if (canMove(c, cell_left)) {
                swapSubstances(cell_left, c);
                return;
            }
        }

        if (canMove(c, cell_right)) {
            swapSubstances(cell_right, c);
            return;
        }
    }
    
    @Override
    public World.Material combustion() {
        return null;
    }
}
