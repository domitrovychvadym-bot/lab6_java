import javax.swing.*;
import java.awt.*;

public class Task1_GeometryCalculator extends JFrame {
    private JComboBox<String> figureComboBox;
    private JLabel labelParam1, labelParam2, resultLabel;
    private JTextField fieldParam1, fieldParam2;
    private JButton calcButton;

    public Task1_GeometryCalculator() {
        setTitle("Завдання 1: Калькулятор фігур (Варіант 7)");
        setSize(420, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        // Ініціалізація компонентів
        add(new JLabel("  Оберіть фігуру:"));
        String[] figures = {"Квадрат", "Прямокутник", "Коло"};
        figureComboBox = new JComboBox<>(figures);
        add(figureComboBox);

        labelParam1 = new JLabel("  Сторона a:");
        fieldParam1 = new JTextField();
        add(labelParam1);
        add(fieldParam1);

        labelParam2 = new JLabel("  Сторона b:");
        fieldParam2 = new JTextField();
        fieldParam2.setEnabled(false); // Для квадрата друге поле не потрібне
        add(labelParam2);
        add(fieldParam2);

        calcButton = new JButton("Розрахувати");
        resultLabel = new JLabel("  Результат: —");
        add(calcButton);
        add(resultLabel);

        // Зміна активності полів при виборі фігури
        figureComboBox.addActionListener(e -> updateFieldsState());

        // Обробка натискання кнопки "Розрахувати"
        calcButton.addActionListener(e -> calculate());
    }

    private void updateFieldsState() {
        String selected = (String) figureComboBox.getSelectedItem();
        fieldParam1.setText("");
        fieldParam2.setText("");
        resultLabel.setText("  Результат: —");

        if ("Квадрат".equals(selected)) {
            labelParam1.setText("  Сторона a:");
            labelParam2.setText("  Сторона b:");
            fieldParam2.setEnabled(false);
        } else if ("Прямокутник".equals(selected)) {
            labelParam1.setText("  Сторона a:");
            labelParam2.setText("  Сторона b:");
            fieldParam2.setEnabled(true);
        } else if ("Коло".equals(selected)) {
            labelParam1.setText("  Радіус r:");
            labelParam2.setText("  Не використовується:");
            fieldParam2.setEnabled(false);
        }
    }

    private void calculate() {
        String selected = (String) figureComboBox.getSelectedItem();
        try {
            double param1 = Double.parseDouble(fieldParam1.getText().trim());
            if (param1 <= 0) {
                JOptionPane.showMessageDialog(this, "Значення має бути більшим за нуль!",
                        "Помилка введення", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double area = 0;
            double perimeter = 0;

            if ("Квадрат".equals(selected)) {
                area = param1 * param1;
                perimeter = 4 * param1;
            } else if ("Прямокутник".equals(selected)) {
                double param2 = Double.parseDouble(fieldParam2.getText().trim());
                if (param2 <= 0) {
                    JOptionPane.showMessageDialog(this, "Обидві сторони мають бути більшими за нуль!",
                            "Помилка введення", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                area = param1 * param2;
                perimeter = 2 * (param1 + param2);
            } else if ("Коло".equals(selected)) {
                area = Math.PI * param1 * param1;
                perimeter = 2 * Math.PI * param1; // Довжина кола
            }

            resultLabel.setText(String.format("  S = %.2f | P = %.2f", area, perimeter));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Введіть коректні числові значення!",
                    "Помилка формату", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Task1_GeometryCalculator().setVisible(true));
    }
}