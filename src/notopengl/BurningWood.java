package notopengl;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class BurningWood extends Substance {
    protected BurningWood() {
        super("burning wood", Float.POSITIVE_INFINITY, new Color(150, 5, 0), 0f, 4000);
    }

    @Override
    public void update(Cell c) {
        if (died(c)) {
            Random r = new Random();
            if (r.nextInt(100) > 80) {
                c.set(World.Material.SMOKE);
            } else {
                c.set(World.Material.ASH);
            }
            return;
        }

        World w = c.w;
        Cell above = w.above(c);
        Cell below = w.below(c);
        Cell cell_left = w.left(c);
        Cell cell_right = w.right(c);
        Cell below_left = w.left(below);
        Cell below_right = w.right(below);

        ArrayList<Cell> tests = new ArrayList<>();
        tests.add(above);
        tests.add(below);
        tests.add(cell_left);
        tests.add(cell_right);
        tests.add(below_left);
        tests.add(below_right);

        for (Cell cell : tests) {
            if (cell != null) {
                if (cell.get().equals(World.Material.WATER) || cell.get().equals(World.Material.DIRTYWATER)) {
                    cell.set(World.Material.STEAM);
                    c.set(World.Material.CHARCOAL);
                }

                if (cell.get().substance.igniteCoeff > 0) {
                    if (ignite(cell.get().substance.igniteCoeff)) {
                        cell.set(cell.get().substance.combustion());
                        return;
                    }
                }
            }
        }
    }

    @Override
    public World.Material combustion() {
        return null;
    }
}
