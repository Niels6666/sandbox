import java.awt.Color;
import java.util.Random;

public class Smoke extends Gaz {

    protected Smoke() {
        super("smoke", 0.05f, new Color(50, 45, 45, 100));
    }

    @Override
    public void update(Cell c) {
        Random r = new Random();
        int test = r.nextInt(2000);
        if (test > 1990) {
            c.set(World.Material.AIR);
        }
        World w = c.w;
        Cell above = w.above(c);
        Cell above_left = w.left(above);
        Cell above_right = w.right(above);
        Cell cell_left = w.left(c);
        Cell cell_right = w.right(c);

        if (chanceToRise(c, above)) {
            swapSubstances(above, c);
            return;
        }

        if (chanceToGoLeftOrRigth()) {
            if (isEmpty(above_left)) {
                swapSubstances(above_left, c);
                return;
            }

            if (isEmpty(above_right)) {
                swapSubstances(above_right, c);
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

}