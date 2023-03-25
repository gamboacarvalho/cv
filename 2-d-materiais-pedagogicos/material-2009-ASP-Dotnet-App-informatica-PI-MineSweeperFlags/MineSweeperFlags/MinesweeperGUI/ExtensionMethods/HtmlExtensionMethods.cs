using System;
using System.Web.Mvc;

namespace MinesweeperGUI.ExtensionMethods
{
    public static class HtmlExtensionMethods
    {
        public static string JavaScriptBooleanValue(this HtmlHelper instance, bool value) { return value.ToString().ToLower(); }
    }
}
