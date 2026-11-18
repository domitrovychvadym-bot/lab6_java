import javax.swing.*;
import java.awt.*;

public class Task2_CinemaTickets extends JFrame {
    private JComboBox<String> movieBox;
    private JRadioButton morningRadio, dayRadio, eveningRadio;
    private JCheckBox popcornCheck, drinkCheck, glasses3DCheck;
    private JTextField ticketsField;

    public Task2_CinemaTickets() {
        setTitle("Завдання 2: Онлайн-замовлення квитків (Варіант 7)");
        setSize(460, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Центральна панель з формою
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));

        // 1. Вибір фільму
        JPanel moviePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        moviePanel.add(new JLabel("Фільм:"));
        String[] movies = {
                "Дюна: Частина друга (150 грн)",
                "Інтерстеллар (140 грн)",
                "Оппенгеймер (160 грн)",
                "Мавка. Лісова пісня (130 грн)"
        };
        movieBox = new JComboBox<>(movies);
        moviePanel.add(movieBox);
        formPanel.add(moviePanel);

        // 2. Вибір сеансу (ButtonGroup)
        JPanel sessionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sessionPanel.setBorder(BorderFactory.createTitledBorder("Час сеансу"));
        morningRadio = new JRadioButton("Ранок (+0 грн)", true);
        dayRadio = new JRadioButton("День (+20 грн)");
        eveningRadio = new JRadioButton("Вечір (+50 грн)");

        ButtonGroup sessionGroup = new ButtonGroup();
        sessionGroup.add(morningRadio);
        sessionGroup.add(dayRadio);
        sessionGroup.add(eveningRadio);

        sessionPanel.add(morningRadio);
        sessionPanel.add(dayRadio);
        sessionPanel.add(eveningRadio);
        formPanel.add(sessionPanel);

        // 3. Додаткові опції (JCheckBox)
        JPanel extrasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        extrasPanel.setBorder(BorderFactory.createTitledBorder("Додатково"));
        popcornCheck = new JCheckBox("Попкорн (+90 грн)");
        drinkCheck = new JCheckBox("Напій (+45 грн)");
        glasses3DCheck = new JCheckBox("3D-окуляри (+30 грн/квиток)");

        extrasPanel.add(popcornCheck);
        extrasPanel.add(drinkCheck);
        extrasPanel.add(glasses3DCheck);
        formPanel.add(extrasPanel);

        // 4. Кількість квитків
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        countPanel.add(new JLabel("Кількість квитків:"));
        ticketsField = new JTextField("1", 6);
        countPanel.add(ticketsField);
        formPanel.add(countPanel);

        add(formPanel, BorderLayout.CENTER);

        // Нижня панель з кнопками
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton buyButton = new JButton("Купити");
        JButton resetButton = new JButton("Скинути");
        buttonPanel.add(buyButton);
        buttonPanel.add(resetButton);
        add(buttonPanel, BorderLayout.SOUTH);

        buyButton.addActionListener(e -> processOrder());
        resetButton.addActionListener(e -> resetForm());
    }

    private void processOrder() {
        int ticketCount;
        try {
            ticketCount = Integer.parseInt(ticketsField.getText().trim());
            if (ticketCount <= 0) {
                JOptionPane.showMessageDialog(this, "Кількість квитків має бути більшою за 0!",
                        "Помилка", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Введіть ціле число квитків!",
                    "Помилка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Базова ціна фільму
        int movieIndex = movieBox.getSelectedIndex();
        int[] basePrices = {150, 140, 160, 130};
        int basePrice = basePrices[movieIndex];

        // Націнка за сеанс
        int sessionExtra = 0;
        String sessionName = "Ранок";
        if (dayRadio.isSelected()) {
            sessionExtra = 20;
            sessionName = "День";
        } else if (eveningRadio.isSelected()) {
            sessionExtra = 50;
            sessionName = "Вечір";
        }

        // Додаткові послуги
        int extrasTotal = 0;
        StringBuilder extrasText = new StringBuilder();
        if (popcornCheck.isSelected()) {
            extrasTotal += 90;
            extrasText.append("Попкорн (90 грн); ");
        }
        if (drinkCheck.isSelected()) {
            extrasTotal += 45;
            extrasText.append("Напій (45 грн); ");
        }
        if (glasses3DCheck.isSelected()) {
            extrasTotal += 30 * ticketCount;
            extrasText.append("3D-окуляри (").append(30 * ticketCount).append(" грн); ");
        }
        if (extrasText.length() == 0) {
            extrasText.append("Немає");
        }

        int totalCost = (basePrice + sessionExtra) * ticketCount + extrasTotal;

        // Формування квитка в діалоговому вікні JDialog
        String ticketInfo = "=== ЕЛЕКТРОННИЙ КВИТОК ===\n"
                + "Фільм: " + movieBox.getSelectedItem() + "\n"
                + "Сеанс: " + sessionName + "\n"
                + "Кількість квитків: " + ticketCount + "\n"
                + "Додатково: " + extrasText + "\n"
                + "--------------------------\n"
                + "ДО СПЛАТИ: " + totalCost + " грн";

        showTicketDialog(ticketInfo);
    }

    private void showTicketDialog(String text) {
        JDialog dialog = new JDialog(this, "Ваш електронний квиток", true);
        dialog.setSize(360, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton okBtn = new JButton("Закрити");
        okBtn.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.add(okBtn);

        dialog.add(new JScrollPane(area), BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void resetForm() {
        movieBox.setSelectedIndex(0);
        morningRadio.setSelected(true);
        popcornCheck.setSelected(false);
        drinkCheck.setSelected(false);
        glasses3DCheck.setSelected(false);
        ticketsField.setText("1");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Task2_CinemaTickets().setVisible(true));
    }
}
