import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class Fire extends Substance {

    protected Fire() {
        super("fire", 0.2f, new Color(240, 85, 15), 0f);
    }

    @Override
    public void update(Cell c, World w) {
        Random r = new Random();
        int test = r.nextInt(100);
        if (test > 99) {
            c.material = World.Material.SMOKE;
        }

        Cell above = w.above(c);
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
                if (cell.material.substance.igniteCoeff > 0) {
                    if (ignite(cell.material.substance.igniteCoeff)) {
                        c.material = World.Material.SMOKE;
                        cell.material = cell.material.substance.combustion();
                        return;
                    }
                }
            }
        }

        if (chanceToRise(c, above)) {
            swapSubstances(above, c);
            return;
        }

        if (chanceToGoLeftOrRigth()) {
            if (isEmpty(below_left)) {
                swapSubstances(below_left, c);
                return;
            }

            if (isEmpty(below_right)) {
                swapSubstances(below_right, c);
            }

            if (isEmpty(cell_left)) {
                swapSubstances(cell_left, c);
                return;
            }

            if (isEmpty(cell_right)) {
                swapSubstances(cell_right, c);
            }

        }
    }

    @Override
    public World.Material combustion() {
        return null;
    }

}
