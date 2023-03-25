package webfast.northwind;

import java.io.PrintStream;

import orm.northwind.Employee;
import webfast.ModelBinder;
import webfast.views.HtmlView;

public class EmployeeDetailsView extends HtmlView{
	public EmployeeDetailsView(){
		head()
			.title("Employee Details");
		body()
			.heading(1, "Employee Details")
			.hr()
			.div()
				.text("Id: ").text(new ModelBinder<Employee>() {
	        public void bind(Employee model, PrintStream out) {out.print(model.getId());}
				})
				.br()
				.text("Name: ").text(new ModelBinder<Employee>() {
	        public void bind(Employee model, PrintStream out) {out.print(model.getFirstName() + " " + model.getLastName());}
				})
				.br().text("Title: ").text(new ModelBinder<Employee>() {
	        public void bind(Employee model, PrintStream out) {out.print(model.getTitle());}
				})
				.br()
				.text("City: ").text(new ModelBinder<Employee>() {
	        public void bind(Employee model, PrintStream out) {out.print(model.getCity());}
				})
				.br()
				.text("Birth date: ").text(new ModelBinder<Employee>() {
	        public void bind(Employee model, PrintStream out) {out.print(model.getBirthDate());}
				})
				.br()
				.a(new ModelBinder<Employee>() {
          public void bind(Employee model, PrintStream out) {
	          out.print(model.getId() + "/orders");
          }
				}).text("Orders");
	}
}
