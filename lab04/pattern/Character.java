public class Character {
    // Модель данных: чистые данные персонажа; формат, возвращаемый шлюзом клиенту
    private final String name, fraction, position, altform, weapon; // final — неизменяемые после создания поля

    public Character(String name, String fraction, String position, String altform, String weapon) {
        this.name = name;
        this.fraction = fraction;
        this.position = position;
        this.altform = altform;
        this.weapon = weapon;
    }

    @Override
    public String toString() {
        return String.format("%s\nСложность: %s | Роль: %s   Прайм-форма: %s \nХарактеристики: %s\n",
                name, fraction, position, altform, weapon);
    }
}
