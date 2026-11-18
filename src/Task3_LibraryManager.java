import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Task3_LibraryManager extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idField, titleField, authorField, yearField;
    private JCheckBox availableCheck;

    public Task3_LibraryManager() {
        setTitle("Завдання 3: Облік книг у бібліотеці (Варіант 7)");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Головне меню (JMenuBar)
        createMenuBar();

        // 2. Таблиця (JTable + DefaultTableModel)
        String[] columns = {"ID", "Назва книги", "Автор", "Рік видання", "Наявність"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Заборона прямого редагування клітинок
            }
        };

        // Початкові тестові дані
        tableModel.addRow(new Object[]{1, "Кобзар", "Тарас Шевченко", 1840, "Так"});
        tableModel.addRow(new Object[]{2, "Тіні забутих предків", "Михайло Коцюбинський", 1911, "Так"});
        tableModel.addRow(new Object[]{3, "Лісова пісня", "Леся Українка", 1912, "Ні"});
        tableModel.addRow(new Object[]{4, "Захар Беркут", "Іван Франко", 1883, "Так"});

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 3. Панель додавання та видалення під таблицею
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        idField = new JTextField(4);
        titleField = new JTextField(14);
        authorField = new JTextField(14);
        yearField = new JTextField(5);
        availableCheck = new JCheckBox("В наявності", true);

        inputPanel.add(new JLabel("ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Назва:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Автор:"));
        inputPanel.add(authorField);
        inputPanel.add(new JLabel("Рік:"));
        inputPanel.add(yearField);
        inputPanel.add(availableCheck);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton addButton = new JButton("Додати книгу");
        JButton deleteButton = new JButton("Видалити обране");
        actionPanel.add(addButton);
        actionPanel.add(deleteButton);

        bottomPanel.add(inputPanel);
        bottomPanel.add(actionPanel);
        add(bottomPanel, BorderLayout.SOUTH);

        // Події кнопок
        addButton.addActionListener(e -> addBook());
        deleteButton.addActionListener(e -> deleteSelectedBook());
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Меню "Файл"
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem clearItem = new JMenuItem("Очистити таблицю");
        JMenuItem exitItem = new JMenuItem("Вихід");

        clearItem.addActionListener(e -> tableModel.setRowCount(0));
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(clearItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Меню "Сортування"
        JMenu sortMenu = new JMenu("Сортування");
        JMenuItem sortByAuthorItem = new JMenuItem("За автором (А-Я)");
        JMenuItem sortByYearItem = new JMenuItem("За роком видання");

        sortByAuthorItem.addActionListener(e -> sortTable(2, false));
        sortByYearItem.addActionListener(e -> sortTable(3, true));

        sortMenu.add(sortByAuthorItem);
        sortMenu.add(sortByYearItem);

        // Меню "Довідка"
        JMenu helpMenu = new JMenu("Довідка");
        JMenuItem aboutItem = new JMenuItem("Про програму");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Система обліку книг у бібліотеці\nЛабораторна робота №6, Варіант 7\nВиконав: студент 2 курсу",
                "Про програму",
                JOptionPane.INFORMATION_MESSAGE
        ));
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(sortMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void addBook() {
        String idText = idField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String yearText = yearField.getText().trim();

        if (idText.isEmpty() || title.isEmpty() || author.isEmpty() || yearText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Заповніть усі поля!",
                    "Попередження", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            int year = Integer.parseInt(yearText);
            String available = availableCheck.isSelected() ? "Так" : "Ні";

            tableModel.addRow(new Object[]{id, title, author, year, available});

            // Очищення полів після додавання
            idField.setText("");
            titleField.setText("");
            authorField.setText("");
            yearField.setText("");
            availableCheck.setSelected(true);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID та Рік видання мають бути цілими числами!",
                    "Помилка формату", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedBook() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Оберіть рядок у таблиці для видалення!",
                    "Попередження", JOptionPane.WARNING_MESSAGE);
            return;
        }
        tableModel.removeRow(selectedRow);
    }

    private void sortTable(int columnIndex, boolean isNumeric) {
        int rowCount = tableModel.getRowCount();
        List<Object[]> rows = new ArrayList<>();

        for (int i = 0; i < rowCount; i++) {
            Object[] row = new Object[tableModel.getColumnCount()];
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                row[j] = tableModel.getValueAt(i, j);
            }
            rows.add(row);
        }

        if (isNumeric) {
            rows.sort(Comparator.comparingInt(r -> Integer.parseInt(r[columnIndex].toString())));
        } else {
            rows.sort(Comparator.comparing(r -> r[columnIndex].toString(), String.CASE_INSENSITIVE_ORDER));
        }

        tableModel.setRowCount(0);
        for (Object[] row : rows) {
            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Task3_LibraryManager().setVisible(true));
    }
}