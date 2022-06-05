package notopengl;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Cell {
    private World.Material material;
    public int row;
    public int col;
    public World w;
    public long life;

    public Cell(World.Material material, int row, int col, World w) {
        this.material = material;
        this.row = row;
        this.col = col;
        this.w = w;
    }

    public void draw(Graphics2D g2d, World w) {
        if(material.substance.color.getAlpha()==0){
            return;
        }
        g2d.setColor(material.substance.color);
        Rectangle2D r = getRect();
        g2d.fill(r);
    }

    public Rectangle2D getRect() {
        return new Rectangle2D.Double(col * w.cw, row * w.ch, w.cw, w.ch);
    }

    public void update() {
        material.consumer.accept(this);
    }

    public boolean isEmpty() {
        return material.equals(World.Material.AIR);
    }

    public void set(Cell other) {
        this.material = other.material;
    }

    public World.Material get(){
        return this.material;
    }

    public void set(World.Material mat){
        this.material = mat;
        life = new Random().nextLong(mat.substance.maxLife);
    }
}
