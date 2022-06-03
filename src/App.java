import javax.swing.Timer;

public class App {
    public static int frames = 0;
    public static long update = 0;
    public static long draw = 0;
    public static long drawnR = 0;

    public static void main(String[] args) throws Exception {
        System.setProperty("sun.java2d.opengl", "True");
        Screen fen = new Screen();
        fen.setVisible(true);

        Timer t = new Timer(1000, (ae) -> {
            String title = String.format("sand simulation| FPS: %03d, update: %d ms, draw: %d ms, rectangles drawn: %d", frames, update, draw, drawnR);
            fen.setTitle(title);
            frames = 0;
        });
        t.setRepeats(true);
        t.start();

        System.out.println("plop");

        while (true) {
            try {
                Thread.sleep(2);
                fen.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
