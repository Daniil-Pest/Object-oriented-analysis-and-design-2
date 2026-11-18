class User:
    def __init__(self, name: str):
        self.name = name
        self.users = []  # Список других пользователей для взаимодействия

    def add_user(self, user: 'User'):
        self.users.append(user)

    def send_message(self, message: str, receiver: 'User'):
        print(f"{self.name} отправляет сообщение '{message}' пользователю {receiver.name}")
        receiver.receive_message(message)

    def receive_message(self, message: str):
        print(f"{self.name} получил сообщение: '{message}'")

if __name__ == "__main__":
    user1 = User("Алиса")
    user2 = User("Боб")
    user3 = User("Чарли")

    # Установка связи между пользователями
    user1.add_user(user2)
    user1.add_user(user3)
    user2.add_user(user1)
    user2.add_user(user3)
    user3.add_user(user1)
    user3.add_user(user2)

    # Отправка сообщений
    user1.send_message("Привет, Боб.", user2)
    user2.send_message("Привет, Алиса.", user1)
    user3.send_message("Привет всем.", user1)
