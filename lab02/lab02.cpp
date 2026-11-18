#include "mainwindow.h"
#include "ui_mainwindow.h"

#include <iostream>
#include <string>
#include <memory>

// Базовый интерфейс Component
class Coffee
{
public:
    virtual double cost() const = 0;
    virtual ~Coffee() = default;
};

// Конкретная реализация Component
class SimpleCoffee : public Coffee
{
public:
    double cost() const override
    {
        return 1.0; // Базовая стоимость кофе
    }
};

// Абстрактный декоратор
class CoffeeDecorator : public Coffee
{
protected: std::unique_ptr<Coffee> coffee;
public:
    CoffeeDecorator(std::unique_ptr<Coffee> coffee) : coffee(std::move(coffee)) {}
    double cost() const override = 0;
};

// Конкретный декоратор для добавления молока
class MilkDecorator : public CoffeeDecorator
{
public:
    MilkDecorator(std::unique_ptr<Coffee> coffee) : CoffeeDecorator(std::move(coffee)) {}
    double cost() const override
    {
        return coffee->cost() + 0.5; // Добавление стоимости молока
    }
};

// Конкретный декоратор для добавления сахара
class SugarDecorator : public CoffeeDecorator
{
public:
    SugarDecorator(std::unique_ptr<Coffee> coffee) : CoffeeDecorator(std::move(coffee)) {}
    double cost() const override
    {
        return coffee->cost() + 0.2; // Добавление стоимости сахара
    }
};



class Brewer
{
public:
    virtual void brew(const std::string &coffee) = 0;
    virtual ~Brewer() = default;
};

class LatteBrewer : public Brewer
{
public:
    void brew(const std::string &coffee) override
    {
        std::cout << "Brewing latte: " << coffee << std::endl;
    }
};

class CappuccinoBrewer : public Brewer
{
public:
    void brew(const std::string &coffee) override
    {
        std::cout << "Brewing cappuccino: " << coffee << std::endl;
    }
};

class MilkLatteBrewer : public Brewer
{
public:
    void brew(const std::string &coffee) override
    {
        std::string milkCoffee = "Milk: " + coffee;
        std::cout << "Brewing milk latte: " << milkCoffee << std::endl;
    }
};

class MilkCappuccinoBrewer : public Brewer
{
public:
    void brew(const std::string &coffee) override
    {
        std::string milkCoffee = "Milk: " + coffee;
        std::cout << "Brewing milk cappuccino: " << milkCoffee << std::endl;
    }
};

class SugarLatteBrewer : public Brewer
{
public:
    void brew(const std::string &coffee) override
    {
        std::cout << "Sugar latte coffee: " << coffee << std::endl;
        std::cout << "Brewing latte: " << coffee << std::endl;
    }
};

class SugarCappuccinoBrewer : public Brewer
{
public:
    void brew(const std::string &coffee) override
    {
        std::cout << "Sugar cappuccino coffee: " << coffee << std::endl;
        std::cout << "Brewing cappuccino: " << coffee << std::endl;
    }
};

MainWindow::MainWindow(QWidget *parent) : QMainWindow(parent), ui(new Ui::MainWindow)
{
    ui->setupUi(this);
}

MainWindow::~MainWindow() { delete ui; }

void MainWindow::on_pushButton_clicked()
{
    std::unique_ptr<Brewer> latteBrewer = std::make_unique<LatteBrewer>();
    latteBrewer->brew("Latte.");

    std::unique_ptr<Brewer> cappuccinoBrewer = std::make_unique<CappuccinoBrewer>();
    cappuccinoBrewer->brew("Cappuccino.");

    std::unique_ptr<Brewer> milkLatteBrewer = std::make_unique<MilkLatteBrewer>();
    milkLatteBrewer->brew("Secret coffee (latte).");

    std::unique_ptr<Brewer> milkCappuccinoBrewer = std::make_unique<MilkCappuccinoBrewer>();
    milkCappuccinoBrewer->brew("Secret coffee (cappuccino).");

    std::unique_ptr<Brewer> sugarLatteBrewer = std::make_unique<SugarLatteBrewer>();
    sugarLatteBrewer->brew("Sugar coffee (latte).");

    std::unique_ptr<Brewer> sugarCappuccinoBrewer = std::make_unique<SugarCappuccinoBrewer>();
    sugarCappuccinoBrewer->brew("Sugar coffee (cappuccino).");
}

void MainWindow::on_pushButton_2_clicked()
{
    // Простой кофе
    std::unique_ptr<Coffee> coffee = std::make_unique<SimpleCoffee>();
    ui->textBrowser->insertPlainText("Декоратор. Simple coffee cost: " + QString::number(coffee->cost()) + "\n");

    // Молоко
    coffee = std::make_unique<MilkDecorator>(std::move(coffee));
    ui->textBrowser->insertPlainText("Декоратор. Milk coffee cost: " + QString::number(coffee->cost()) + "\n");

    // Сахар
    coffee = std::make_unique<SugarDecorator>(std::move(coffee));
    ui->textBrowser->insertPlainText("Декоратор. Milk-sugar coffee cost: " + QString::number(coffee->cost()) + "\n");
    //ui->textBrowser->insertPlainText("Milk-sugar coffee cost: %1.\n".QString::number(coffee->cost());
}

//

/**
 * Базовый интерфейс Компонента определяет поведение, которое изменяется
 * декораторами.
 */
class Component {
public:
    virtual ~Component() {}
    virtual std::string Operation() const = 0;
};
/**
 * Конкретные Компоненты предоставляют реализации поведения по умолчанию. Может
 * быть несколько вариаций этих классов.
 */
class ConcreteComponent : public Component {
public:
    std::string Operation() const override {
        return "ConcreteComponent";
    }
};
/**
 * Базовый класс Декоратора следует тому же интерфейсу, что и другие компоненты.
 * Основная цель этого класса - определить интерфейс обёртки для всех конкретных
 * декораторов. Реализация кода обёртки по умолчанию может включать в себя поле
 * для хранения завёрнутого компонента и средства его инициализации.
 */
class Decorator : public Component {
    /**
     * @var Component
     */
    protected:
        Component* component_;
        
public:
    Decorator(Component* component) : component_(component) {
    }
    /**
     * Декоратор делегирует всю работу обёрнутому компоненту.
     */
    std::string Operation() const override {
        return this->component_->Operation();
    }
};
/**
 * Конкретные Декораторы вызывают обёрнутый объект и изменяют его результат
 * некоторым образом.
 */
class ConcreteDecoratorA : public Decorator {
    /**
     * Декораторы могут вызывать родительскую реализацию операции, вместо того,
     * чтобы вызвать обёрнутый объект напрямую. Такой подход упрощает расширение
     * классов декораторов.
     */
    public:
        ConcreteDecoratorA(Component* component) : Decorator(component) {
        }
        std::string Operation() const override {
            return "ConcreteDecoratorA(" + Decorator::Operation() + ")";
        }
};
/**
 * Декораторы могут выполнять своё поведение до или после вызова обёрнутого
 * объекта.
 */
class ConcreteDecoratorB : public Decorator {
public:
    ConcreteDecoratorB(Component* component) : Decorator(component) {
    }
    
    std::string Operation() const override {
        return "ConcreteDecoratorB(" + Decorator::Operation() + ")";
    }
};
/**
 * Клиентский код работает со всеми объектами, используя интерфейс Компонента.
 * Таким образом, он остаётся независимым от конкретных классов компонентов, с
 * которыми работает.
 */
void ClientCode(Component* component) {
    // ...
    std::cout << "RESULT: " << component->Operation();
    // ...
}

int main() {
    /**
     * Таким образом, клиентский код может поддерживать как простые компоненты...
     */
    Component* simple = new ConcreteComponent;
    std::cout << "Client: I've got a simple component:\n";
    ClientCode(simple);
    std::cout << "\n\n";
    /**
     * ...так и декорированные.
     *
     * Обратите внимание, что декораторы могут обёртывать не только простые
     * компоненты, но и другие декораторы.
     */
    Component* decorator1 = new ConcreteDecoratorA(simple);
    Component* decorator2 = new ConcreteDecoratorB(decorator1);
    std::cout << "Client: Now I've got a decorated component:\n";
    ClientCode(decorator2);
    std::cout << "\n";
    
    delete simple;
    delete decorator1;
    delete decorator2;
}
