import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class DirectAccessApp extends JFrame {
    
    private static final String DB_URL = "jdbc:sqlite:warframe.db";
    
    private JTextField inputField;
    private JTextArea outputArea;

    public DirectAccessApp() {
        this.setTitle("Warframe Heroes (без паттерна Gateway)");
        this.setSize(580, 460);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo((Component) null);
        this.setLayout(new BorderLayout(10, 10));

        // Панель управления (верхняя часть)
        JPanel topPanel = new JPanel(new FlowLayout());
        this.inputField = new JTextField(22);
        JButton btnSearch = new JButton("Найти по имени");
        JButton btnListAll = new JButton("Список всех");
        
        topPanel.add(new JLabel("Имя героя:"));
        topPanel.add(this.inputField);
        topPanel.add(btnSearch);
        topPanel.add(btnListAll);
        this.add(topPanel, BorderLayout.NORTH);

        // Область вывода
        this.outputArea = new JTextArea();
        this.outputArea.setEditable(false);
        this.outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        this.outputArea.setMargin(new Insets(10, 10, 10, 10));
        this.add(new JScrollPane(this.outputArea), BorderLayout.CENTER);

        // Инициализация БД при запуске
        this.initDatabaseFromSQL();

        // Логика кнопки "Найти" (JDBC-код внутри слушателя событий)
        btnSearch.addActionListener((e) -> {
            String name = this.inputField.getText().trim();
            if (!name.isEmpty()) {
                this.outputArea.setText("Поиск…\n");

                try {
                    // Прямое создание соединения
                    Connection conn = DriverManager.getConnection(DB_URL);

                    try {
                        String sql = "SELECT * FROM characters WHERE LOWER(name) = LOWER(?)";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, name);
                        ResultSet rs = ps.executeQuery();
                        
                        // Прямая работа с ResultSet для форматирования вывода
                        if (rs.next()) {
                            this.outputArea.setText(formatRow(rs));
                        } else {
                            this.outputArea.setText("Герой не найден в архиве.");
                        }
                    } catch (Throwable ex) {
                        if (conn != null) {
                            try { conn.close(); } catch (Throwable ignored) {}
                        }
                        throw ex;
                    }

                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    this.outputArea.setText("Ошибка БД: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        // Логика кнопки "Список всех" (JDBC-код внутри слушателя событий)
        btnListAll.addActionListener((e) -> {
            this.outputArea.setText(" Загрузка списка…\n");
            StringBuilder sb = new StringBuilder("Все герои Warframe:\n" + "─".repeat(50) + "\n");

            try {
                // Прямое создание соединения
                Connection conn = DriverManager.getConnection(DB_URL);

                try {
                    Statement stmt = conn.createStatement();

                    try {
                        ResultSet rs = stmt.executeQuery("SELECT * FROM characters");

                        try {
                            while (rs.next()) {
                                sb.append(formatRow(rs)).append("\n");
                            }
                            this.outputArea.setText(sb.toString());
                        } catch (Throwable ex) {
                            if (rs != null) {
                                try { rs.close(); } catch (Throwable ignored) {}
                            }
                            throw ex;
                        }

                        if (rs != null) {
                            rs.close();
                        }
                    } catch (Throwable ex) {
                        if (stmt != null) {
                            try { stmt.close(); } catch (Throwable ignored) {}
                        }
                        throw ex;
                    }

                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (Throwable ex) {
                    if (conn != null) {
                        try { conn.close(); } catch (Throwable ignored) {}
                    }
                    throw ex;
                }

                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                this.outputArea.setText("Ошибка БД: " + ex.getMessage());
            }
        });
    }

    /**
     * Метод инициализации БД.
     * В варианте без паттерна этот метод также находится внутри GUI-класса.
     */
    private void initDatabaseFromSQL() {
        this.outputArea.append("Инициализация базы данных…\n");

        try {
            Connection conn = DriverManager.getConnection(DB_URL);

            label104: {
                label115: {
                    try {
                        ResultSet meta = conn.getMetaData().getTables(null, null, "characters", null);
                        if (meta.next()) {
                            this.outputArea.append("Таблица characters уже существует — пропуск создания.\n");
                            break label115;
                        }

                        InputStream is = this.getClass().getResourceAsStream("/warframe_data.sql");
                        if (is == null) {
                            File f = new File("warframe_data.sql");
                            if (!f.exists()) {
                                this.outputArea.append("Файл warframe_data.sql не найден.\n");
                                this.outputArea.append("Положите его в корневую папку проекта рядом с .class файлами.\n");
                                break label104;
                            }
                            is = new FileInputStream(f);
                        }

                        String script = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                        String[] statements = script.split(";");
                        Statement stmt = conn.createStatement();

                        try {
                            for (String sql : statements) {
                                String clean = sql.replaceAll("--.*", "").trim();
                                if (!clean.isEmpty()) {
                                    stmt.execute(clean);
                                }
                            }
                        } catch (Throwable ex) {
                            if (stmt != null) {
                                try { stmt.close(); } catch (Throwable ignored) {}
                            }
                            throw ex;
                        }

                        if (stmt != null) {
                            stmt.close();
                        }

                        this.outputArea.append("База данных успешно создана и заполнена из warframe_data.sql\n");
                    } catch (Throwable ex) {
                        if (conn != null) {
                            try { conn.close(); } catch (Throwable ignored) {}
                        }
                        throw ex;
                    }

                    if (conn != null) {
                        conn.close();
                    }
                    return;
                }

                if (conn != null) {
                    conn.close();
                }
                return;
            }

            if (conn != null) {
                conn.close();
            }

        } catch (Exception ex) {
            this.outputArea.append("Ошибка инициализации БД: " + ex.getMessage() + "\n");
            ex.printStackTrace();
        }
    }

    /**
     * Форматирование строки результата.
     * Интерпретация полей БД под тематику Warframe
     * fraction -> Сложность
     * position -> Роль
     * altform -> Прайм-форма
     * weapon -> Характеристики
     */
    private String formatRow(ResultSet rs) throws SQLException {
        return String.format("%s\nСложность: %s | Роль: %s   Прайм-форма: %s \nХарактеристики: %s\n", 
                rs.getString("name"), 
                rs.getString("fraction"), 
                rs.getString("position"), 
                rs.getString("altform"), 
                rs.getString("weapon"));
    }

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException var2) {
        }

        SwingUtilities.invokeLater(() -> (new DirectAccessApp()).setVisible(true));
    }
}
