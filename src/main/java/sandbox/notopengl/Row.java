package sandbox.notopengl;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Row {
    ArrayList<Cell> cells;

    public Row(int ncellsX, int row, World w) {
        cells = new ArrayList<>(ncellsX);
        for (int i = 0; i < ncellsX; i++) {
            cells.add(new Cell(World.Material.AIR, row, i, w));
        }
    }

    public void draw(Graphics2D g2d) {
        Cell prev = cells.get(0);
        Rectangle2D rect = new Rectangle2D.Double();
        Color color = prev.get().substance.color;
        rect.setFrame(prev.getRect());
        for (int i = 1; i < cells.size(); i++) {
            Cell c = cells.get(i);
            if (c.get().equals(prev.get())) {
                rect.setFrame(rect.createUnion(c.getRect()));
                color = c.get().substance.color;
                g2d.setColor(color);
                prev = c;
            } else {
                if (color.getAlpha() > 0) {
                    g2d.setColor(color);
                    g2d.fill(rect);
                    App.drawnR++;
                }
                    rect.setFrame(c.getRect());
                    prev = c;
            }
        }
        if (prev.get().substance.color.getAlpha() > 0) {
            g2d.setColor(prev.get().substance.color);
            g2d.fill(rect);
            App.drawnR++;
        }
    }

}
