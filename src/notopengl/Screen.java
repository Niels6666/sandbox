package notopengl;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class Screen extends JFrame {
    public World w;
    boolean mousePressed = false;
    boolean updating = true;
    int index = 0;

    BufferedImage img;

    public Screen() {
        setTitle("sand simulation");
        setMinimumSize(new Dimension(800, 600));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            img = ImageIO.read(new File("bg.png"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        w = new World(0, 0, 3, 3);

        JPanel tools = new JPanel();
        tools.setLayout(new BoxLayout(tools, BoxLayout.X_AXIS));
        tools.setBackground(Color.white);

        JLabel mchosen = new JLabel("| air");
        mchosen.setBackground(Color.white);

        tools.add(Box.createHorizontalStrut(10));
        for (World.Material m : World.Material.values()) {
            Icon i = new Icon() {
                @Override
                public int getIconHeight() {
                    return 30;
                }

                @Override
                public int getIconWidth() {
                    return 30;
                }

                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    g.setColor(m.substance.color);
                    g.fillRect(x, y, 30, 30);
                }
            };

            JButton b = new JButton(i) {
                ActionListener l;

                @Override
                public void addActionListener(java.awt.event.ActionListener l) {
                    super.addActionListener(l);
                    this.l = l;
                }

                @Override
                public void doClick(int pressTime) {
                    l.actionPerformed(null);
                }
            };

            b.setToolTipText(m.substance.name);
            b.setBorder(new LineBorder(m.equals(World.Material.AIR) ? new Color(200, 0, 200) : Color.DARK_GRAY, 2));
            b.setBackground(m.substance.color);
            b.setForeground(Color.lightGray);
            b.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    w.material = m;
                    mchosen.setText("| " + m.substance.name);

                    for (Component c : tools.getComponents()) {
                        if (c instanceof JButton) {
                            ((JComponent) c).setBorder(new LineBorder(Color.DARK_GRAY, 2));
                        }
                    }
                    b.setBorder(new LineBorder(new Color(200, 0, 200), 2));
                }
            });

            tools.add(b);
            tools.add(Box.createHorizontalStrut(10));
        }
        tools.add(mchosen);

        tools.add(Box.createHorizontalStrut(30));

        JButton clear = new JButton("clear grid");
        clear.setBackground(Color.white);
        clear.setBorder(new Border() {
            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(5, 5, 5, 5);
            }

            @Override
            public boolean isBorderOpaque() {
                return true;
            }

            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.darkGray);
                g2d.setStroke(new BasicStroke(2f));
                g2d.draw(new Rectangle2D.Double(x, y, width, height));
            }
        });

        clear.addActionListener((ae) -> {
            w.clear();
        });

        tools.add(clear);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                float pw = getWidth();
                float ph = getHeight();

                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(img, 0, 0, getWidth(), getHeight(), 0, 0, getWidth(), getHeight(), null);
                g.setColor(new Color(60, 90, 60, 150));
                g.fillRect(0, 0, getWidth(), getHeight());

                Point2D mp = getMousePosition();
                if (mp != null) {
                    if (mousePressed) {
                        w.click(mp.getX(), mp.getY());
                    }
                }

                if (updating) {
                    long start = System.currentTimeMillis();
                    int ncellsX = (int) Math.floor(pw / w.cw) - 1;
                    int ncellsY = (int) Math.floor(ph / w.ch);
                    w.update(ncellsX, ncellsY);
                    App.update = System.currentTimeMillis() - start;
                } else {
                    App.update = -1;
                }
                long start = System.currentTimeMillis();
                w.draw(g2d, mp, (int) (pw), (int) (ph));
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

                for (Component c : tools.getComponents()) {
                    if (c instanceof JButton) {
                        if (c.getBackground().equals(w.material.substance.color)) {
                            ((AbstractButton) c).doClick(0);
                            return;
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.requestFocus();
            }
        };
        panel.addMouseListener(ml);
        panel.addMouseMotionListener(ml);
        panel.addMouseWheelListener(ml);

        panel.requestFocus();
        panel.setBorder(null);

        // panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_S)),
        // "switchLeft");
        // panel.getActionMap().put("switchLeft", new AbstractAction() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // var list = World.Material.values();
        // index++;
        // if (index < 0) {
        // index = 0;
        // }
        // if (index > list.length - 1) {
        // index = list.length - 1;
        // }
        // w.material = list[index];
        // }

        // @Override
        // public boolean isEnabled() {
        // return true;
        // }
        // });

        // panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_Z)),
        // "switchRight");
        // panel.getActionMap().put("switchRight", new AbstractAction() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // var list = World.Material.values();
        // index--;
        // if (index < 0) {
        // index = 0;
        // }
        // if (index > list.length - 1) {
        // index = list.length - 1;
        // }
        // w.material = list[index];
        // }

        // @Override
        // public boolean isEnabled() {
        // return true;
        // }
        // });

        // panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_C)),
        // "clear");
        // panel.getActionMap().put("clear", new AbstractAction() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // w.clear();
        // }

        // @Override
        // public boolean isEnabled() {
        // return true;
        // }
        // });

        // panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_U)),
        // "update");
        // panel.getActionMap().put("update", new AbstractAction() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // if (!updating) {
        // w.update();
        // }
        // }

        // @Override
        // public boolean isEnabled() {
        // return true;
        // }
        // });

        // panel.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_P)),
        // "stop");
        // panel.getActionMap().put("stop", new AbstractAction() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // updating = !updating;
        // }

        // @Override
        // public boolean isEnabled() {
        // return true;
        // }
        // });

        JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tools, panel);
        setContentPane(pane);
    }

}
