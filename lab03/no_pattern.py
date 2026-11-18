from abc import ABC, abstractmethod

# Интерфейс посредника
class Mediator(ABC):
    @abstractmethod
    def send_message(self, message: str, user: 'ChatUser'):
        pass

# Конкретный посредник (чат-сервер)
class ChatMediator(Mediator):
    def __init__(self):
        self.users = []

    def add_user(self, user: 'ChatUser'):
        self.users.append(user)

    def send_message(self, message: str, sender: 'ChatUser'):
        for user in self.users:
            if user != sender:
                user.receive_message(message)

# Интерфейс пользователя
class ChatUser(ABC):
    def __init__(self, mediator: Mediator):
        self.mediator = mediator

    @abstractmethod
    def send_message(self, message: str):
        pass

    @abstractmethod
    def receive_message(self, message: str):
        pass

# Конкретный пользователь
class ConcreteUser(ChatUser):
    def __init__(self, name: str, mediator: Mediator):
        super().__init__(mediator)
        self.name = name
        mediator.add_user(self)

    def send_message(self, message: str):
        print(f"{self.name} отправляет сообщение: {message}")
        self.mediator.send_message(message, self)

    def receive_message(self, message: str):
        print(f"{self.name} получил сообщение: {message}")

# Пример использования
if __name__ == "__main__":
    mediator = ChatMediator()

    user1 = ConcreteUser("Алиса", mediator)
    user2 = ConcreteUser("Боб", mediator)
    user3 = ConcreteUser("Чарли", mediator)

    user1.send_message("Привет, Боб.")
    user2.send_message("Привет, Алиса.")
    user3.send_message("Привет всем.")

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
