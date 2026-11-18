import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Task4_PenDrawing extends JFrame {
    private Color currentColor = Color.BLACK;
    private float currentThickness = 3.0f;
    private DrawPanel drawPanel;

    public Task4_PenDrawing() {
        setTitle("Завдання 4: Малювання пером (Варіант 7)");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        drawPanel = new DrawPanel();
        add(drawPanel, BorderLayout.CENTER);

        createMenu();
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Меню "Файл"
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem clearItem = new JMenuItem("Очистити полотно");
        JMenuItem exitItem = new JMenuItem("Вихід");

        clearItem.addActionListener(e -> drawPanel.clearCanvas());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(clearItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Меню "Колір пера"
        JMenu colorMenu = new JMenu("Колір пера");
        JMenuItem blackItem = new JMenuItem("Чорний");
        JMenuItem redItem = new JMenuItem("Червоний");
        JMenuItem blueItem = new JMenuItem("Синій");
        JMenuItem greenItem = new JMenuItem("Зелений");
        JMenuItem customColorItem = new JMenuItem("Обрати інший колір...");

        blackItem.addActionListener(e -> currentColor = Color.BLACK);
        redItem.addActionListener(e -> currentColor = Color.RED);
        blueItem.addActionListener(e -> currentColor = Color.BLUE);
        greenItem.addActionListener(e -> currentColor = new Color(0, 153, 51));
        customColorItem.addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(this, "Оберіть колір пера", currentColor);
            if (chosen != null) {
                currentColor = chosen;
            }
        });

        colorMenu.add(blackItem);
        colorMenu.add(redItem);
        colorMenu.add(blueItem);
        colorMenu.add(greenItem);
        colorMenu.addSeparator();
        colorMenu.add(customColorItem);

        // Меню "Товщина лінії"
        JMenu thicknessMenu = new JMenu("Товщина лінії");
        ButtonGroup thicknessGroup = new ButtonGroup();

        JRadioButtonMenuItem thinItem = new JRadioButtonMenuItem("Тонка (2 px)");
        JRadioButtonMenuItem mediumItem = new JRadioButtonMenuItem("Середня (4 px)", true);
        JRadioButtonMenuItem thickItem = new JRadioButtonMenuItem("Товста (8 px)");
        JRadioButtonMenuItem extraThickItem = new JRadioButtonMenuItem("Дуже товста (14 px)");

        thinItem.addActionListener(e -> currentThickness = 2.0f);
        mediumItem.addActionListener(e -> currentThickness = 4.0f);
        thickItem.addActionListener(e -> currentThickness = 8.0f);
        extraThickItem.addActionListener(e -> currentThickness = 14.0f);

        thicknessGroup.add(thinItem);
        thicknessGroup.add(mediumItem);
        thicknessGroup.add(thickItem);
        thicknessGroup.add(extraThickItem);

        thicknessMenu.add(thinItem);
        thicknessMenu.add(mediumItem);
        thicknessMenu.add(thickItem);
        thicknessMenu.add(extraThickItem);

        menuBar.add(fileMenu);
        menuBar.add(colorMenu);
        menuBar.add(thicknessMenu);
        setJMenuBar(menuBar);
    }

    // Внутрішній клас панелі для малювання пером
    private class DrawPanel extends JPanel {
        private BufferedImage canvas;
        private Graphics2D g2d;
        private int prevX, prevY;

        public DrawPanel() {
            setBackground(Color.WHITE);

            MouseAdapter mouseHandler = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    prevX = e.getX();
                    prevY = e.getY();
                    drawLineSegment(prevX, prevY, prevX, prevY);
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    int currX = e.getX();
                    int currY = e.getY();
                    drawLineSegment(prevX, prevY, currX, currY);
                    prevX = currX;
                    prevY = currY;
                }
            };

            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
        }

        private void initCanvas() {
            if (canvas == null || canvas.getWidth() != getWidth() || canvas.getHeight() != getHeight()) {
                BufferedImage newCanvas = new BufferedImage(
                        Math.max(getWidth(), 1),
                        Math.max(getHeight(), 1),
                        BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D g = newCanvas.createGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, newCanvas.getWidth(), newCanvas.getHeight());
                if (canvas != null) {
                    g.drawImage(canvas, 0, 0, null);
                }
                g.dispose();
                canvas = newCanvas;
            }
        }

        private void drawLineSegment(int x1, int y1, int x2, int y2) {
            initCanvas();
            g2d = canvas.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(currentColor);
            g2d.setStroke(new BasicStroke(currentThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(x1, y1, x2, y2);
            g2d.dispose();
            repaint();
        }

        public void clearCanvas() {
            if (canvas != null) {
                Graphics2D g = canvas.createGraphics();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                g.dispose();
                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            initCanvas();
            g.drawImage(canvas, 0, 0, null);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Task4_PenDrawing().setVisible(true));
    }
}