using System;
using System.Collections.Generic;
using System.Reflection;
using System.Text;

namespace MinesweeperUtils
{
    public class JSon
    {
        JSon() { }

        static string GetPropertyValue(object value)
        {
            Type type = value.GetType();

            if (type == typeof(System.DateTime))
            {
                TimeSpan ts = (DateTime)value - DateTime.Parse("1/1/1970");
                return string.Format("new Date({0})", ts.TotalMilliseconds.ToString());
            }
            else if (type == typeof(System.String))
            {
                return string.Format("new String(\"{0}\")", EscapeStringForJavaScript((string)value));
            }
            else if (type == typeof(System.Int16) || type == typeof(System.Int32) || type == typeof(System.Int64) || type == typeof(System.Decimal) ||
                      type == typeof(System.Double) || type == typeof(System.Single))
            {
                return string.Format("new Number({0})", value.ToString());
            }
            else if (type.IsArray)
            {
                bool first = true;
                StringBuilder rObj = new StringBuilder();
                rObj.Append("[");
                foreach (object o in (Array)value)
                {
                    rObj.AppendFormat("{0}{1}", (first ? "" : ", "), GetPropertyValue(o));
                    first = false;
                }

                rObj.Append("]");

                return rObj.ToString();
            }
                
            return EscapeStringForJavaScript(value.ToString());
        }


        static string EscapeStringForJavaScript(string input)
        {
            input = input.Replace("", @"");
            input = input.Replace("\b", @"\b");
            input = input.Replace("\t", @"\t");
            input = input.Replace("\n", @"\n");
            input = input.Replace("\f", @"\f");
            input = input.Replace("\r", @"\r");
            input = input.Replace("\"", @"""");
            return input;
        }

        public static string GetJSonEntity<T>(T obj, List<string> properties)
        {
            if (properties == null) throw new ArgumentNullException("properties");
            if (obj == null) throw new ArgumentNullException("obj");


            StringBuilder rObject = new StringBuilder();
            Type type = typeof(T);
            for (int i = 0; i < properties.Count; i++)
            {
                PropertyInfo pi = type.GetProperty(properties[i]);
                rObject.AppendFormat("{0}\"{1}\":{2}", (i == 0 ? "" : ","), properties[i], GetPropertyValue(pi.GetValue(obj, null)));
            }

            return rObject.ToString();
        }


        public static string GetJSonEntity<T>( T obj )
        {
            if (obj == null) throw new ArgumentNullException("obj");

            Type type = typeof(T);
            FieldInfo[] fi = type.GetFields();
            StringBuilder rObject = new StringBuilder();
            for (int i = 0; i < fi.Length; i++)
            {
                rObject.AppendFormat("{0}\"{1}\":{2}", (i == 0 ? "" : ","), fi[i].Name, fi[i].GetValue(obj));
            }

            return rObject.ToString();
        }
    }
}
