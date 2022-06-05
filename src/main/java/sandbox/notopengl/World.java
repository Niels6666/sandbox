package sandbox.notopengl;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import java.util.function.Consumer;
import java.awt.geom.Point2D;

public class World {
    public enum Mode {
        ADD, REMOVE;
    }

    public enum Material {
        AIR(new Air()),
        SAND(new Sand()),
        WATER(new Water()),
        FIRE(new Fire()),
        SMOKE(new Smoke()),
        STEAM(new Steam()),
        WOOD(new Wood()),
        WALL(new Wall()),
        BURNINGWOOD(new BurningWood()),
        ASH(new Ash()),
        DIRTYWATER(new DirtyWater()),
        CHARCOAL(new Charcoal());

        public Consumer<Cell> consumer;
        public Substance substance;

        private Material(Substance updater) {
            substance = updater;
            consumer = new Consumer<Cell>() {
                @Override
                public void accept(Cell c) {
                    updater.update(c);
                }
            };
        }
    }

    public Grid grid;
    public Mode mode;
    public Material material;
    public int ncellsX;
    public int ncellsY;
    public int cw;
    public int ch;

    public World(int ncellsX, int ncellsY, int cw, int ch) {
        this.ncellsX = ncellsX;
        this.ncellsY = ncellsY;
        this.cw = cw;
        this.ch = ch;
        this.mode = Mode.ADD;
        this.material = Material.SAND;
        this.grid = new Grid(ncellsX, ncellsY, this);
    }

    public void click(double x, double y) {
        for (Cell c : grid.mouseOnGroup(x, y, this)) {
            switch (mode) {
                case ADD:
                    if (c.get().equals(Material.AIR)) {
                        c.set(this.material);
                    }
                    break;
                case REMOVE:
                    if (c.get().equals(this.material)) {
                        c.set(Material.AIR);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * @return the cell below c, or None if there isn't
     */
    public Cell below(Cell c) {
        if (c == null) {
            return null;
        }
        int cr = c.row;
        int cc = c.col;

        if (cr + 1 < grid.rows.size()) {
            Row row_below = grid.rows.get(cr + 1);
            Cell cell_below = row_below.cells.get(cc);
            return cell_below;
        }
        return null;
    }

    /**
     * 
     * @return the cell above c, or None if there isn't
     */
    public Cell above(Cell c) {
        if (c == null) {
            return null;
        }
        int cr = c.row;
        int cc = c.col;

        if (cr > 0) {
            Cell cell_above = grid.rows.get(cr - 1).cells.get(cc);
            return cell_above;
        }
        return null;
    }

    /**
     * 
     * @return the cell to the left of c, or None if there isn't
     */
    public Cell left(Cell c) {
        if (c == null) {
            return null;
        }
        int cr = c.row;
        int cc = c.col;
        Row current_row = grid.rows.get(cr);

        if (cc > 0) {
            Cell cell_left = current_row.cells.get(cc - 1);
            return cell_left;
        }
        return null;
    }

    /**
     * 
     * @return the cell to the right of c, or None if there isn't
     */
    public Cell right(Cell c) {
        if (c == null) {
            return null;
        }

        int cr = c.row;
        int cc = c.col;

        Row current_row = grid.rows.get(cr);

        if (cc + 1 < current_row.cells.size()) {
            Cell cell_right = current_row.cells.get(cc + 1);
            return cell_right;
        }
        return null;
    }

    public void draw(Graphics2D g2d, Point2D mp, int width, int height) {
        g2d.setStroke(new BasicStroke(1.0f));
        // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        // RenderingHints.VALUE_ANTIALIAS_ON);
        String sub = "";

        grid.draw(g2d);

        for (Cell c : grid.toArray()) {
            if (mp != null) {
                double mx = mp.getX();
                double my = mp.getY();
                Rectangle2D cr = c.getRect();

                if (cr.contains(mx, my)) {
                    sub = (c.col + 1) + ";" + (c.row + 1);
                    sub += " : " + c.get().substance.name;
                }
            }
        }
        g2d.setStroke(new BasicStroke(1f));

        if (mp != null) {
            double mx = mp.getX();
            double my = mp.getY();
            Color color = new Color(0, 0, 0, 0);
            switch (mode) {
                case ADD:
                    color = new Color(0, 255, 0);
                    break;
                case REMOVE:
                    color = new Color(255, 0, 0);
                    break;
                default:
                    break;
            }
            g2d.setColor(color);
            Rectangle2D r = new Rectangle2D.Double();
            r.setFrameFromCenter(mx, my, mx + cw * 2, my + ch * 2);
            g2d.draw(r);
        }
        g2d.setColor(Color.lightGray);
        g2d.draw(new Rectangle2D.Double(0, 0, cw * ncellsX, ch * ncellsY));

        g2d.setFont(new Font("Consolas", Font.PLAIN, 20));

        Rectangle2D subR = g2d.getFont().getStringBounds(sub, g2d.getFontRenderContext());
        String info = (mode.equals(Mode.ADD) ? "adding " : "removing ") + material.substance.name;
        Rectangle2D infoR = g2d.getFont().getStringBounds(info, g2d.getFontRenderContext());

        g2d.drawString(info, (float) (width - infoR.getWidth() - cw * 2f),
                (float) (height - infoR.getHeight() - subR.getHeight() - ch * 2f));

        g2d.drawString(sub, (float) (width - subR.getWidth() - cw * 2f),
                (float) (height - subR.getHeight() - ch * 2f));

        // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        // RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    public void update() {
        for (Cell c : grid.toArray()) {
            if (c.get().substance instanceof Gaz) {
                c.update();
            }
        }

        for (Cell c : grid.toUpdateArray()) {
            if (!(c.get().substance instanceof Gaz)) {
                c.update();
            }
        }
    }

    public void update(int ncellsX, int ncellsY) {
        if (ncellsX == this.ncellsX && ncellsY == this.ncellsY) {
            update();
            return;
        }

        Grid g = new Grid(ncellsX, ncellsY, this);
        if (ncellsX < this.ncellsX || ncellsY < this.ncellsY) {
            this.grid.printOn(g, ncellsX, ncellsY);
        } else {
            this.grid.printOn(g, this.ncellsX, this.ncellsY);
        }

        this.ncellsX = ncellsX;
        this.ncellsY = ncellsY;
        this.grid = g;
        update();
    }

    public void clear() {
        for (Cell c : grid.toArray()) {
            c.set(Material.AIR);
        }
    }
}
