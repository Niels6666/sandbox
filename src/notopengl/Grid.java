package notopengl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;

public class Grid {
    ArrayList<Row> rows;

    public Grid(int ncellsX, int ncellsY, World w) {
        rows = new ArrayList<>(ncellsY);
        for (int i = 0; i < ncellsY; i++) {
            rows.add(new Row(ncellsX, i, w));
        }
    }

    public ArrayList<Cell> toArray() {
        ArrayList<Cell> res = new ArrayList<>();
        for (Row r : rows) {
            res.addAll(List.copyOf(r.cells));
        }
        return res;
    }

    public ArrayList<Cell> toUpdateArray() {
        ArrayList<Row> list = new ArrayList<>(rows);
        Collections.reverse(list);

        ArrayList<Cell> res = new ArrayList<>();
        for (Row r : list) {
            res.addAll(r.cells);
        }
        return res;
    }

    public Cell mouseOn(double x, double y, World w) {
        for (Cell c : toArray()) {
            if (c.getRect().contains(x, y)) {
                return c;
            }
        }
        return null;
    }

    public List<Cell> mouseOnGroup(double x, double y, World w) {
        List<Cell> res = new ArrayList<>();
        Rectangle2D rect = new Rectangle2D.Double();
        rect.setFrameFromCenter(x, y, x + w.cw * 2, y + w.ch * 2);
        for (Cell c : toArray()) {
            if (c.getRect().intersects(rect)) {
                res.add(c);
            }
        }
        return res;
    }

    // public void setCell(Cell c) {
    //     int col = c.col;
    //     int row = c.row;
    //     rows.get(row).cells.get(col).set(c);
    // }

    public Cell getCell(Cell c){
        int col = c.col;
        int row = c.row;
        return rows.get(row).cells.get(col);
    }


    public void printOn(Grid g, int w, int h){
        for(Cell c: toArray()){
            if(c.col < w && c.row < h){
                g.getCell(c).set(c);
            }
        }
    }

    public void draw(Graphics2D g2d) {
        App.drawnR = 0;
        for(Row r : rows){
            r.draw(g2d);
        }
    }
}
