import javax.swing.*;
import java.awt.*;
import java.util.List;

// Клиент. Знает только о шлюзе и его публичных методах, не содержит ни одной строки SQL, JDBC или ResultSet
public class GatewayApp extends JFrame {
    private final CharacterGateway gateway; // Зависимость только от шлюза
    private JTextField inputField;
    private JTextArea outputArea;

    public GatewayApp() {
        this.gateway = new CharacterGateway();
        setupUI();
    }

    private void setupUI() {
        // Функция настройки окна, кнопок и полей
        setTitle("Warframe Heroes (с паттерном Gateway)");
        setSize(580, 460);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Панель управления
        JPanel topPanel = new JPanel(new FlowLayout());
        inputField = new JTextField(22);
        JButton btnSearch = new JButton("Найти по имени");
        JButton btnListAll = new JButton("Список всех");
        topPanel.add(new JLabel("Имя персонажа:"));
        topPanel.add(inputField);
        topPanel.add(btnSearch);
        topPanel.add(btnListAll);
        add(topPanel, BorderLayout.NORTH);

        // Область вывода
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputArea.setMargin(new Insets(10, 10, 10, 10));
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Логика кнопок: делегирование шлюзу
        btnSearch.addActionListener(e -> {
            String name = inputField.getText().trim();
            if (name.isEmpty()) return;
            outputArea.setText("Поиск через шлюз…\n");
            Character result = gateway.findByName(name); // Делегирование шлюзу для подключения к БД
            outputArea.setText(result != null ? " *" + result : "Персонаж не найден в архиве.");
        });

        btnListAll.addActionListener(e -> {
            outputArea.setText("Загрузка списка через шлюз…\n");
            List<Character> list = gateway.findAll(); // Делегирование
            StringBuilder sb = new StringBuilder("Все персонажи:\n" + "─".repeat(50) + "\n");
            list.forEach(c -> sb.append(c).append("\n"));
            outputArea.setText(sb.toString());
        });
    }
}
