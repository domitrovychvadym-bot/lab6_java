import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Task5_MemoryGame extends JFrame {
    private static final int GRID_SIZE = 4; // Поле 4x4 (16 карток)
    private JButton[] buttons = new JButton[GRID_SIZE * GRID_SIZE];
    private List<String> cardValues = new ArrayList<>();
    private boolean[] matched = new boolean[GRID_SIZE * GRID_SIZE];

    private int firstIndex = -1;
    private int secondIndex = -1;
    private boolean isWaiting = false; // Блокування кліків під час таймера
    private int moves = 0;
    private int pairsFound = 0;

    private JLabel statusLabel;

    public Task5_MemoryGame() {
        setTitle("Завдання 5: Гра «Меморі» (Картки пам'яті)");
        setSize(460, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Верхня панель інформації
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        statusLabel = new JLabel("Ходів: 0 | Знайдено пар: 0 / 8");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        JButton restartButton = new JButton("Нова гра");
        restartButton.addActionListener(e -> initGame());

        topPanel.add(statusLabel);
        topPanel.add(restartButton);
        add(topPanel, BorderLayout.NORTH);

        // Ігрове поле 4x4
        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE, 8, 8));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            buttons[i] = new JButton("?");
            buttons[i].setFont(new Font("SansSerif", Font.BOLD, 16));
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(e -> handleCardClick(index));
            gridPanel.add(buttons[i]);
        }

        add(gridPanel, BorderLayout.CENTER);

        initGame();
    }

    private void initGame() {
        String[] words = {"JAVA", "SWING", "CODE", "FRAME", "PANEL", "TIMER", "EVENT", "CLASS"};
        cardValues.clear();
        for (String word : words) {
            cardValues.add(word);
            cardValues.add(word); // Кожного слова по 2 штуки
        }
        Collections.shuffle(cardValues);

        firstIndex = -1;
        secondIndex = -1;
        isWaiting = false;
        moves = 0;
        pairsFound = 0;

        for (int i = 0; i < buttons.length; i++) {
            matched[i] = false;
            buttons[i].setText("?");
            buttons[i].setBackground(null);
            buttons[i].setEnabled(true);
        }
        updateStatus();
    }

    private void handleCardClick(int index) {
        if (isWaiting || matched[index] || index == firstIndex) {
            return;
        }

        // Відкриваємо натиснуту картку
        buttons[index].setText(cardValues.get(index));

        if (firstIndex == -1) {
            firstIndex = index;
        } else {
            secondIndex = index;
            moves++;
            updateStatus();
            checkMatch();
        }
    }

    private void checkMatch() {
        if (cardValues.get(firstIndex).equals(cardValues.get(secondIndex))) {
            // Пара збіглася — залишаємо відкритими
            matched[firstIndex] = true;
            matched[secondIndex] = true;
            buttons[firstIndex].setBackground(new Color(180, 240, 180));
            buttons[secondIndex].setBackground(new Color(180, 240, 180));

            pairsFound++;
            firstIndex = -1;
            secondIndex = -1;
            updateStatus();

            if (pairsFound == (GRID_SIZE * GRID_SIZE) / 2) {
                JOptionPane.showMessageDialog(this,
                        "Вітаємо! Ви знайшли всі пари за " + moves + " ходів!",
                        "Перемога", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            // Пара не збіглася — закриваємо через javax.swing.Timer
            isWaiting = true;
            Timer timer = new Timer(800, e -> {
                buttons[firstIndex].setText("?");
                buttons[secondIndex].setText("?");
                firstIndex = -1;
                secondIndex = -1;
                isWaiting = false;
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void updateStatus() {
        statusLabel.setText("Ходів: " + moves + " | Знайдено пар: " + pairsFound + " / 8");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Task5_MemoryGame().setVisible(true));
    }
}
