package webfast.northwind;

import orm.northwind.Order;
import webfast.views.HtmlBody;
import webfast.views.HtmlTable;
import webfast.views.HtmlTr;
import webfast.views.HtmlView;

public class OrdersListView extends HtmlView{
	public OrdersListView(Iterable<Order> orders) {
		head()
			.title("Orders");
		
		HtmlBody body = body()
			.heading(1, "List of Orders")
			.hr();
		HtmlTable table = body.table();
		HtmlTr tr = table.tr();
		tr.th().text("Id");
		tr.th().text("Name");
		tr.th().text("Adress");
		tr.th().text("City");
		for (Order e : orders) {
	    tr = table.tr();
	    tr.td().text("" + e.getId());
	    tr.td().a("/orders/" + e.getId()).text(e.getShipName());
	    tr.td().text(e.getShipAddress());
	    tr.td().text(e.getShipCity());
	  }
  }
}
