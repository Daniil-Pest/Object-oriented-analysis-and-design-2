using System;
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
		
		private void button1_Click(object sender, EventArgs e)
		{
			Pizza pizza = new Pizza
				(comboBox1.Text,
				comboBox2.Text,
				checkBox1.Checked,
				checkBox2.Checked,
				checkBox3.Checked,
				checkBox4.Checked);
			
			richTextBox1.Text += $"Конструктор: объект класса Pizza создан с размером {pizza.Size}, коркой {pizza.Crust}, колбасой ({pizza.Sausage}), сыром ({pizza.Cheese}), грибами ({pizza.Mushrooms}) и оливками ({pizza.Olives}).\n";
		}
		
		private void button2_Click(object sender, EventArgs e)
		{
			Pizza pizza = new PizzaBuilder()
				.size(comboBox1.Text)
				.crust(comboBox2.Text)
				.sausage(checkBox1.Checked)
				.cheese(checkBox2.Checked)
				.mushrooms(checkBox3.Checked)
				.olives(checkBox4.Checked)
				.build();
			
			richTextBox1.Text += $"Строитель: объект класса Pizza создан с размером {pizza.Size}, коркой {pizza.Crust}, колбасой ({pizza.Sausage}), сыром ({pizza.Cheese}), грибами ({pizza.Mushrooms}) и оливками ({pizza.Olives}).\n";
		}
	}
}

public class Pizza
{
	public string Size { get; set; }
	public string Crust { get; set; }
	public bool Sausage { get; set; }
	public bool Cheese { get; set; }
	public bool Mushrooms { get; set; }
	public bool Olives { get; set; }
	
	public Pizza(string size, string crust, bool sausage, bool cheese, bool mushrooms, bool olives)
	{
		Size = size;
		Crust = crust;
		Sausage = sausage;
		Cheese = cheese;
		Mushrooms = mushrooms;
		Olives = olives;
	}
}

public class PizzaBuilder
{
	private string Size;
	private string Crust;
	private bool Sausage;
	private bool Cheese;
	private bool Mushrooms;
	private bool Olives;
	
	public PizzaBuilder size(string size)
	{
		this.Size = size;
		
		return this;
	}
	
	public PizzaBuilder crust(string crust)
	{
		this.Crust = crust;
		
		return this;
	}
	
	public PizzaBuilder sausage(bool sausage)
	{
		this.Sausage = sausage;
		
		return this;
	}
	
	public PizzaBuilder cheese(bool cheese)
	{
		this.Cheese = cheese;
		
		return this;
	}
	
	public PizzaBuilder mushrooms(bool mushrooms)
	{
		this.Mushrooms = mushrooms;
		
		return this;
	}
	
	public PizzaBuilder olives(bool olives)
	{
		this.Olives = olives;
		
		return this;
	}
	
	public Pizza build()
	{
		return new Pizza(Size, Crust, Sausage, Cheese, Mushrooms, Olives);
	}
}
