<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<RepositoryInterfaces.DataObjects.ISession>" %>
<%@ Import Namespace="RepositoryInterfaces.DataObjects" %>
<h3>
    Esta sessão ocorrerá em
    <%=Model.Date %>
    na
    <%=Model.Room.Id %>!<br />
    Preço por bilhete:
    <%=Model.TicketPrice %>€
</h3>
<div id="layout">
    <%
        IRoom room = Model.Room;
        IDictionary<int, IPreReserve> preReserves = Page.Session["PreReserves"] as IDictionary<int, IPreReserve>;
        IPreReserve preReserve = null;
        if (preReserves != null)
            preReserves.TryGetValue(Model.Id, out preReserve);
        for (int i = 0; i < room.Rows; i++)
        {
    %>
    <span class="row">
        <%for (int j = 0; j < room.Seats; j++)
          {
              int offset = i * room.Seats + j;
              if (room.Layout[offset] == 1)
              {
                  if (Model.UsedSeats.Contains(offset))
                  {	
        %>
        <span class="chair reserved" id="<%=offset %>" onclick="chairClicked(<%=offset %>, <%=Model.Id %>)">
        </span>
        <%
}
                  else
                  {
                      if (preReserve != null && preReserve.Seats.Contains(offset))
                      {	
        %>
        <span class="chair free selected" id="<%=offset %>" onclick="chairClicked(<%=offset %>, <%=Model.Id %>)">
        </span>
        <%
}
                      else
                      {
        %>
        <span class="chair free" id="<%=offset %>" onclick="chairClicked(<%=offset %>, <%=Model.Id %>)">
        </span>
        <%
}
                  }
              }
              else
              {
        %>
        <span class="chair empty"></span>
        <%
            }

          } %>
    </span>
    <%
}
    %>
    <br />
    <div id="legend">
        <div>
            <span class='chair free'></span><b>Lugar livre;</b></div>
        <div>
            <span class='chair reserved'></span><b>Lugar ocupado;</b></div>
        <div>
            <span class='chair selected'></span><b>Lugar a aguardar reserva;</b></div>
        <br />
    </div>
    <br />
</div>
