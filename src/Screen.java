import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Screen extends JFrame {
    public World w;
    boolean mousePressed = false;
    boolean updating = true;
    int index = 0;

    public Screen() {
        setTitle("sand simulation");
        setMinimumSize(new Dimension(800, 600));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        w = new World(0, 0, 3, 3);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(60, 90, 30));
                g.fillRect(0, 0, getWidth(), getHeight());

                
                Point2D mp = getMousePosition();
                if (mp != null) {
                    if (mousePressed) {
                        w.click(mp.getX(), mp.getY());
                    }
                }

                float pw = getWidth();
                float ph = getHeight();
                if (updating) {
                    long start = System.currentTimeMillis();
                    int ncellsX = (int) Math.floor(pw / w.cw) - 1;
                    int ncellsY = (int) Math.floor(ph / w.ch);
                    w.update(ncellsX, ncellsY);
                    App.update = System.currentTimeMillis() - start;
                }
                long start = System.currentTimeMillis();
                w.draw((Graphics2D) g, mp, (int) (pw), (int) (ph));
                App.draw = System.currentTimeMillis() - start;
                App.frames++;
            }
        };

        MouseAdapter ml = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePressed = true;
                if (e.getButton() == MouseEvent.BUTTON1) {
                    w.mode = World.Mode.ADD;
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    w.mode = World.Mode.REMOVE;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed = false;
                if (e.getButton() == MouseEvent.BUTTON1) {
                    w.mode = World.Mode.ADD;
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    w.mode = World.Mode.REMOVE;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    w.mode = World.Mode.ADD;
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    w.mode = World.Mode.REMOVE;
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    w.mode = World.Mode.ADD;
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    w.mode = World.Mode.REMOVE;
                }
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rot = e.getWheelRotation();
                var list = World.Material.values();
                index += rot;
                if (index < 0) {
                    index = 0;
                }
                if (index > list.length - 1) {
                    index = list.length - 1;
                }
                w.material = list[index];
            }
        };
        panel.addMouseListener(ml);
        panel.addMouseMotionListener(ml);
        panel.addMouseWheelListener(ml);

        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_S)), "switchLeft");
        panel.getActionMap().put("switchLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var list = World.Material.values();
                index++;
                if (index < 0) {
                    index = 0;
                }
                if (index > list.length - 1) {
                    index = list.length - 1;
                }
                w.material = list[index];
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        });

        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_Z)), "switchRight");
        panel.getActionMap().put("switchRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var list = World.Material.values();
                index--;
                if (index < 0) {
                    index = 0;
                }
                if (index > list.length - 1) {
                    index = list.length - 1;
                }
                w.material = list[index];
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        });

        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_C)), "clear");
        panel.getActionMap().put("clear", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                w.clear();
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        });

        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_U)), "update");
        panel.getActionMap().put("update", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!updating) {
                    w.update();
                }
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        });

        panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_P)), "stop");
        panel.getActionMap().put("stop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updating = !updating;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        });

        panel.requestFocus();
        panel.setBorder(null);
        setContentPane(panel);
    }
}
