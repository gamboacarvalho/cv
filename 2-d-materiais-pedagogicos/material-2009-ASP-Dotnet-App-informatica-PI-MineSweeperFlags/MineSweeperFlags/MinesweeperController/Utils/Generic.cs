using System;
using System.Collections.Generic;
using Minesweeper;

namespace MinesweeperControllers.Utils
{
    public class Generic
    {
        Generic() { }

        public static int GetInt(string intValue)
        {
            int rValue;
            int.TryParse(intValue, out rValue);
            return rValue;
        }

        public static String GetJSon<T>(T obj) where T : IToJSon
        {
            if (obj == null)
                return "[]";
            return ("[" + obj.ToJSon() + "]");
        }

        public static String GetJSon<T>(List<T> list) where T : IToJSon
        {
            if (list == null || list.Count == 0)
                return "[]";
            String retJSon = "";
            list.ForEach(x => retJSon += x.ToJSon() + ",");
            retJSon = "[" + retJSon.Substring(0, retJSon.Length - 1) + "]";
            return retJSon;
        }
    }
}
