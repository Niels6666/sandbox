import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class BurningWood extends Substance {
    protected BurningWood() {
        super("burning wood", Float.POSITIVE_INFINITY, new Color(150, 5, 0), 0.0004f);
    }

    @Override
    public void update(Cell c, World w) {
        Random r = new Random();
        int test = r.nextInt(10000);
        if (test > 9990) {
            c.material = World.Material.SMOKE;
        }
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
                if (cell.material.substance.igniteCoeff > 0) {
                    if (ignite(cell.material.substance.igniteCoeff)) {
                        if (r.nextInt(100) > 70) {
                            cell.material = World.Material.FIRE;
                        } else {
                            cell.material = cell.material.substance.combustion();
                        }
                        return;
                    }else{
                        if(cell.material.equals(World.Material.WATER)){
                            cell.material = World.Material.STEAM;
                            c.material = World.Material.CHARCOAL;
                        }
                    }
                }
            }
        }
    }

    @Override
    public World.Material combustion() {
        return World.Material.ASH;
    }
}
