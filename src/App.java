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
            StringBuilder sb = new StringBuilder("sand simulation|");
            String fpsS = String.format("fps: %03d", frames);
            String updateS = "update: paused";
            if (update > 0) {
                updateS = String.format(", update: %d ms", update);
            }
            String drawS = String.format(", draw: %d ms", draw);
            String drawnRS = String.format(", rectangles drawn: %d", drawnR);

            String title = sb.append(fpsS).append(updateS).append(drawS).append(drawnRS).toString();
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
