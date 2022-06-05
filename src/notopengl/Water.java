package notopengl;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Water extends Substance {

    protected Water() {
        super("water", 0.9f, new Color(0, 10, 150, 100), 0f, 1);
    }

    @Override
    public void update(Cell c) {
        World w = c.w;
        Cell above = w.above(c);
        Cell below = w.below(c);
        Cell below_left = w.left(below);
        Cell below_right = w.right(below);

        Cell below_left2 = w.below(w.left(below_left));
        Cell below_right2 = w.below(w.right(below_right));

        Cell cell_left = w.left(c);
        Cell cell_right = w.right(c);

        ArrayList<Cell> tests = new ArrayList<>();
        tests.add(above);
        tests.add(below);
        tests.add(cell_left);
        tests.add(cell_right);
        tests.add(below_left);
        tests.add(below_right);

        for (Cell cell : tests) {
            if (cell != null) {
                if (cell.get().equals(World.Material.ASH)) {
                    cell.set(World.Material.DIRTYWATER);
                    c.set(World.Material.AIR);
                    return;
                }
                if (cell.get().equals(World.Material.FIRE)) {
                    cell.set(World.Material.SMOKE);
                    c.set(World.Material.STEAM);
                }
            }
        }

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

        if (canMove(c, cell_left)) {
            swapSubstances(cell_left, c);
            return;
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
