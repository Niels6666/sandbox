package sandbox.notopengl;
import java.awt.Color;
import java.util.Random;

public abstract class Substance {
    public String name;
    public float mass;
    public Color color;
    public float igniteCoeff;
    public long maxLife;
    private Random gen;

    protected Substance(String name, float mass, Color color, float igniteCoeff, long maxLife) {
        this.name = name;
        this.mass = mass;
        this.color = color;
        this.igniteCoeff = igniteCoeff;
        this.maxLife = maxLife;
        gen = new Random(System.currentTimeMillis());
    }

    public abstract void update(Cell c);

    public static boolean isEmpty(Cell c) {
        return c != null && c.isEmpty();
    }

    public static boolean isHeavier(Cell a, Cell b) {
        return (a != null && b != null) && a.get().substance.mass > b.get().substance.mass;
    }

    protected boolean canMove(Cell c, Cell other) {
        return isEmpty(other) || isHeavier(c, other);
    }

    protected void swapSubstances(Cell a, Cell b) {
        World.Material sa = a.get();
        World.Material sb = b.get();
        a.set(sb);
        b.set(sa);
    }

    protected boolean chanceToRise(Cell c, Cell above) {
        if (isHeavier(above, c)) {
            float res = gen.nextFloat(2f);
            float chance = above.get().substance.mass - c.get().substance.mass;
            return res > chance;
        }
        return false;
    }

    protected boolean chanceToGoLeft() {
        return gen.nextInt(100) > 5;
    }

    protected boolean chanceToGoLeftOrRigth() {
        return gen.nextInt(100) < 50;
    }

    protected boolean ignite(float igniteCoeff) {
        Random r = new Random();
        return r.nextInt(1000) < igniteCoeff * 1000;
    }

    protected boolean died(Cell c) {
        if(c.life <= 0){
            return true;
        }else{
            c.life--;
            return false;
        }
    }

    /**
     * @return the product of substance combustion
     */
    public abstract World.Material combustion();
}