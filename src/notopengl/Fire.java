package notopengl;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Fire extends Gaz {

    protected Fire() {
        super("fire", 0.2f, 80, new Color(240, 85, 15));
    }

    @Override
    public void update(Cell c) {
        if (died(c)) {
            c.set(World.Material.SMOKE);
        }

        World w = c.w;
        Cell above = w.above(c);
        Cell above_left = w.left(above);
        Cell above_right = w.right(above);
        Cell below = w.below(c);
        Cell cell_left = w.left(c);
        Cell cell_right = w.right(c);
        Cell below_left = w.left(below);
        Cell below_right = w.right(below);

        ArrayList<Cell> tests = new ArrayList<>();
        tests.add(below);
        tests.add(cell_left);
        tests.add(cell_right);
        tests.add(below_left);
        tests.add(below_right);

        for (Cell cell : tests) {
            if (cell != null) {
                if (cell.get().substance.igniteCoeff > 0) {
                    if (ignite(cell.get().substance.igniteCoeff)) {
                        c.set(World.Material.SMOKE);
                        cell.set(cell.get().substance.combustion());
                        return;
                    }
                }
            }
        }

        // if (chanceToRise(c, above)) {
        // swapSubstances(above, c);
        // return;
        // }

        // if (chanceToGoLeftOrRigth()) {
        // if (isEmpty(below_left)) {
        // swapSubstances(below_left, c);
        // return;
        // }

        // if (isEmpty(below_right)) {
        // swapSubstances(below_right, c);
        // }

        // if (isEmpty(cell_left)) {
        // swapSubstances(cell_left, c);
        // return;
        // }

        // if (isEmpty(cell_right)) {
        // swapSubstances(cell_right, c);
        // }

        // }
        if (canMove(c, above)) {
            swapSubstances(above, c);
            return;
        }

        if (canMove(c, above_left)) {
            swapSubstances(above_left, c);
            return;
        }

        if (canMove(c, above_right)) {
            swapSubstances(above_right, c);
            return;
        }

        if (canMove(c, w.left(cell_left))) {
            swapSubstances(cell_left, c);
            return;
        }

        if (canMove(c, cell_right)) {
            swapSubstances(cell_right, c);
            return;
        }
    }
}
