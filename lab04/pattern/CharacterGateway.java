import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Инкапсуляция доступа к другой системе или источнику данных; клиент не знает о JDBC, SQL-скриптах, структуре таблиц или драйверах
public class CharacterGateway {
    private static final String DB_URL = "jdbc:sqlite:warframe.db"; 

    public CharacterGateway() {
        initDatabase(); // Автоматическая инициализация при создании шлюза
    }

    // Инициализация БД из внешнего SQL-файла 
    private void initDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Проверка, создана ли таблица
            ResultSet meta = conn.getMetaData().getTables(null, null, "characters", null);
            if (meta.next()) return; // Таблица уже есть — скрипт не нужен

            InputStream is = getClass().getResourceAsStream("/warframe_data.sql");
            if (is == null) {
                File f = new File("warframe_data.sql");
                if (!f.exists()) throw new FileNotFoundException("Файл warframe_data.sql не найден в папке запуска.");
                is = new FileInputStream(f);
            }

            // Чтение скрипта
            String script = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            String[] statements = script.split(";");

            try (Statement stmt = conn.createStatement()) {
                for (String sql : statements) {
                    String clean = sql.replaceAll("--.*", "").trim();
                    if (!clean.isEmpty()) stmt.execute(clean);
                }
            }
            System.out.println("БД создана и заполнена из warframe_data.sql");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка инициализации шлюза: " + e.getMessage(), e);
        }
    }

    // Единый метод поиска для клиента
    public Character findByName(String name) {
        String sql = "SELECT * FROM characters WHERE LOWER(name) = LOWER(?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name); // Безопасная подстановка параметра
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs); // Преобразование строки БД в объект Character
        } catch (SQLException e) {
            throw new RuntimeException("Шлюз: ошибка поиска.", e);
        }
        return null; 
    }

    // Единый метод для получения списка для клиента
    public List<Character> findAll() {
        List<Character> list = new ArrayList<>();
        String sql = "SELECT * FROM characters";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs)); // Маппинг каждой строки
        } catch (SQLException e) {
            throw new RuntimeException("Шлюз: ошибка выборки.", e);
        }
        return list;
    }

    // Внутренний маппинг: ResultSet → объект (скрыт от клиента)
    private Character mapRow(ResultSet rs) throws SQLException {
        return new Character(
            rs.getString("name"), rs.getString("fraction"),
            rs.getString("position"), rs.getString("altform"),
            rs.getString("weapon")
        );
    }
}
