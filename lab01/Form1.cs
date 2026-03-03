using System;
using System.Text;
using System.Windows.Forms;

namespace lab01
{
	public partial class Form1 : Form
	{
		public Form1()
		{
			InitializeComponent();
		}
		
		private void Form1_Load(object sender, EventArgs e)
		{
			comboBox1.Text = comboBox1.Items[2].ToString();
			comboBox2.Text = comboBox2.Items[0].ToString();
		}
		
		class Size { public string size { get; set; } }
		class Crust { public string crust { get; set; } }
		class Sausage { public bool sausage { get; set; } }
		class Cheese { public bool cheese { get; set; } }
		class Mushrooms { public bool mushrooms { get; set; } }
		class Olives { public bool olives { get; set; } }
		
		class Pizza
		{
			public Size size { get; set; }
			public Crust crust { get; set; }
			public Sausage sausage { get; set; }
			public Cheese cheese { get; set; }
			public Mushrooms mushrooms { get; set; }
			public Olives olives { get; set; }
			
			public override string ToString()
			{
				StringBuilder String = new StringBuilder();
				
				String.Append("размером " + size.size + ", ");
				String.Append("коркой " + (crust.crust == "None" ? "(" : "") + crust.crust + (crust.crust == "None" ? ")" : "") + ", ");
				String.Append("колбасой (" + sausage.sausage + "), ");
				String.Append("сыром (" + cheese.cheese + "), ");
				String.Append("грибами (" + mushrooms.mushrooms + ") и ");
				String.Append("оливками (" + olives.olives + ")");
				
				return String.ToString();
			}
		}
		
		abstract class PizzaBuilder
		{
			public Pizza pizza { get; private set; }
			
			public void BuildPizza() { pizza = new Pizza(); }
			
			public abstract void Size(string size);
			public abstract void Crust(string crust);
			public abstract void Sausage(bool sausage);
			public abstract void Cheese(bool cheese);
			public abstract void Mushrooms(bool mushrooms);
			public abstract void Olives(bool olives);
		}
		
		class ThePizzaBuilder : PizzaBuilder
		{
			public override void Size(string size)
			{
				this.pizza.size = new Size { size = size };
			}
			
			public override void Crust(string crust)
			{
				this.pizza.crust = new Crust { crust = crust };
			}
			
			public override void Sausage(bool sausage)
			{
				this.pizza.sausage = new Sausage { sausage = sausage };
			}
			
			public override void Cheese(bool cheese)
			{
				this.pizza.cheese = new Cheese { cheese = cheese };
			}
			
			public override void Mushrooms(bool mushrooms)
			{
				this.pizza.mushrooms = new Mushrooms { mushrooms = mushrooms };
			}
			
			public override void Olives(bool olives)
			{
				this.pizza.olives = new Olives { olives = olives };
			}
		}
		
		class Baker
		{
			public Pizza Bake(PizzaBuilder pizzaBuilder,
							string size,
							string crust,
							bool sausage,
							bool cheese,
							bool mushrooms,
							bool olives)
			{
				pizzaBuilder.BuildPizza();
				
				pizzaBuilder.Size(size);
				pizzaBuilder.Crust(crust);
				pizzaBuilder.Sausage(sausage);
				pizzaBuilder.Cheese(cheese);
				pizzaBuilder.Mushrooms(mushrooms);
				pizzaBuilder.Olives(olives);
				
				return pizzaBuilder.pizza;
			}
		}
		
		private void button1_Click(object sender, EventArgs e)
		{
			Pizza pizza = new Pizza
			{
				size = new Size { size = comboBox1.Text },
				crust = new Crust { crust = comboBox2.Text },
				sausage = new Sausage { sausage = checkBox1.Checked },
				cheese = new Cheese { cheese = checkBox2.Checked },
				mushrooms = new Mushrooms { mushrooms = checkBox3.Checked },
				olives = new Olives { olives = checkBox4.Checked },
			};
			
			richTextBox1.Text += "Конструктор: объект класса Pizza создан с " + pizza.ToString() + ".\n";
		}
		
		private void button2_Click(object sender, EventArgs e)
		{
			Baker baker = new Baker();
			PizzaBuilder builder = new ThePizzaBuilder();
			Pizza pizza = baker.Bake(builder,
								comboBox1.Text,
								comboBox2.Text,
								checkBox1.Checked,
								checkBox2.Checked,
								checkBox3.Checked,
								checkBox4.Checked);
			
			richTextBox1.Text += "Строитель: объект класса Pizza создан с " + pizza.ToString() + ".\n";
		}
	}
}
