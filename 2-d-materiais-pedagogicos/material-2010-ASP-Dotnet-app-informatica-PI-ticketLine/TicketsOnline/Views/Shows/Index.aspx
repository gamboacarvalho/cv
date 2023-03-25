<%@ Page Title="" Language="C#" Inherits="System.Web.Mvc.ViewPage<TicketsOnline.Models.ShowsSelectorControlModel>"
    MasterPageFile="~/Views/Shared/Site.Master" %>

<%@ Import Namespace="TicketsOnline.Models" %>
<asp:Content runat="server" ID="Content" ContentPlaceHolderID="TitleContent">
    Espectáculos</asp:Content>
<asp:Content runat="server" ID="Content1" ContentPlaceHolderID="HeaderContent">
    <link href="<%=Url.Content("~/Content/Styles/Shows.css") %>" rel="stylesheet" type="text/css" />
    <script src="<%=Url.Content("~/Scripts/MicrosoftMvcValidation.js") %>" type="text/javascript"></script>
    <script src="<%=Url.Content("~/Scripts/Views/Room.js") %>" type="text/javascript"></script>
    <script type="text/javascript">
        STAR_COUNT = <%=StarRatingControlModel.StarCount %>;

        if (document.images) {
            starOff = new Image(12, 12);
            starOff.src = '<%=Url.Content("~/Content/Images/Grey_Star_small.gif")%>';

            starOn = new Image(12, 12);
            starOn.src = '<%=Url.Content("~/Content/Images/Red_Star_small.gif")%>';
        }
    </script>
</asp:Content>
<asp:Content runat="server" ID="Content2" ContentPlaceHolderID="MainContent">
    <%Html.EnableClientValidation(); %>
    <div id='show_session'>
        <div>
            <div id='show'>
                <%Html.RenderPartial(
                      "ShowsSelectorControl",
                      Model);%>
            </div>
            <div id='session'>
            </div>
        </div>
    </div>
</asp:Content>
