import java.awt.Color;
import java.util.Random;

public class DirtyWater extends Substance {
    protected DirtyWater() {
        super("dirty water", 1.2f, new Color(10, 40, 110), 0.001f);
    }

    @Override
    public void update(Cell c, World w) {
        Cell above = w.above(c);
        Cell below = w.below(c);
        Cell below_left = w.left(below);
        Cell below_right = w.right(below);
        Cell cell_left = w.left(c);
        Cell left_left = w.left(cell_left);
        Cell cell_right = w.right(c);
        Cell right_right = w.left(cell_right);

        if (chanceToRise(c, above)) {
            swapSubstances(above, c);
            return;
        }

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
        
        if (chanceToGoLeftOrRigth()) {
            
            if (isEmpty(right_right)) {
                swapSubstances(right_right, c);
            }
            
            if (isEmpty(left_left)) {
                swapSubstances(left_left, c);
                return;
            }
            
        }
    }
    
    @Override
    public World.Material combustion() {
        Random r = new Random();
        if (r.nextInt(100) > 70) {
            return World.Material.STEAM;
        } else {
            return World.Material.SMOKE;
        }
    }
}
