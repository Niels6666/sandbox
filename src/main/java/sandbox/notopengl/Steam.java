package sandbox.notopengl;
import java.awt.Color;

public class Steam extends Gaz {

    protected Steam() {
        super("steam", 0.08f, 1, new Color(200, 200, 200));
    }

    @Override
    public void update(Cell c) {
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
