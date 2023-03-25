package webfast.northwind;

import orm.northwind.Employee;
import webfast.views.HtmlBody;
import webfast.views.HtmlTable;
import webfast.views.HtmlTr;
import webfast.views.HtmlView;

public class EmployeesListView extends HtmlView{
	public EmployeesListView(Iterable<Employee> emps) {
		head()
			.title("Employees")
			.linkCss("/resources/themes/base/jquery.ui.all.css")
			.scriptLink("/resources/jquery-1.5.1.js")
			.scriptLink("/resources/jquery.ui.core.js")
			.scriptLink("/resources/jquery.ui.widget.js") 
			.scriptLink("/resources/jquery.ui.datepicker.js")
			.scriptLink("/resources/jquery.ui.datepicker-pt.js")
			.scriptBlock().code("$(function() {$(\"#datepickerBirth\" ).datepicker();});");
		
		HtmlBody body = body()
			.heading(1, "List of Employees")
			.hr();
		HtmlTable table = body.table();
		HtmlTr tr = table.tr();
		tr.th().text("Id");
		tr.th().text("Name");
		tr.th().text("Title");
		tr.th().text("City");
		for (Employee e : emps) {
	    tr = table.tr();
	    tr.td().text("" + e.getId());
	    tr.td().a("/employees/" + e.getId()).text(e.getFirstName() + " " + e.getLastName());
	    tr.td().text(e.getCity());
	    tr.td().text(e.getTitle());
	  }
		body
			.hr()
			.heading(2, "New employee:")
			.form("employees")
				.text("First name: ").inputText("firstName").br()
				.text("Last name: ").inputText("lastName").br()
				.text("Title: ").inputText("title").br()
				.text("City: ").inputText("city").br()
				.text("Birthd date: ").inputText("birthDate", "datepickerBirth").br()
				.inputSubmit("Create");
  }
}
