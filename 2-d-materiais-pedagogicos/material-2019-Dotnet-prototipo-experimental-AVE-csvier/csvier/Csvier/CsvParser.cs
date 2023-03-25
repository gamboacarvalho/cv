using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;

namespace Csvier
{
    public class CsvParser
    {
        private readonly Type klass;
        private readonly char separator = ',';
        private List<string> lines;
        private List<int> ctorArgs;
        

        public CsvParser(Type klass, char separator)
        {
            this.separator = separator;
            this.klass = klass;
            this.ctorArgs = new List<int>();
        }
        public CsvParser(Type klass) : this(klass, ',')
        {
        }

        public CsvParser CtorArg(string arg, int col)
        {
            ctorArgs.Add(col);
            return this;
        }

        public CsvParser PropArg(string arg, int col)
        {
            return this;
        }

        public CsvParser FieldArg(string arg, int col)
        {
            return this;
        }

        public CsvParser Load(String src)
        {
            string[] arr = src.Split(new[] { "\r\n", "\r", "\n" }, StringSplitOptions.None);
            lines = new List<string>(arr);
            return this;
        }

        public CsvParser Remove(int count)
        {
            while (count-- > 0) lines.RemoveAt(0);
            return this;
        }

        public CsvParser RemoveEmpties()
        {
            for (int i = 0; i < lines.Count; i++)
            {
                if (lines[i].Equals(""))
                    lines.RemoveAt(i--);

            }
            return this;
        }

        public object[] Parse()
        {
            List<object> res = new List<object>();
            ConstructorInfo ctor = klass.GetConstructors()[0];
            ParameterInfo[] argTypes = ctor.GetParameters();
            foreach (string l in lines)
            {
                res.Add(Parse(l, ctor, argTypes));
            }
            return res.ToArray();
        }

        private object Parse(string l, ConstructorInfo ctor, ParameterInfo[] argTypes)
        {
            string[] words = l.Split(separator);
            string[] args = ctorArgs.Select(idx => words[idx]).ToArray();
            object[] vals = Parse(args, argTypes);
            return ctor.Invoke(vals);
        }

        private object[] Parse(string[] args, ParameterInfo[] argTypes)
        {
            int i = 0;
            object[] vals = new object[args.Length];
            foreach (ParameterInfo p in argTypes)
            {
                if (p.ParameterType == typeof(string)) { 
                    vals[i] = args[i];
                }
                else { 
                    MethodInfo m = p.ParameterType.GetMethod("Parse", new Type[] { typeof(string) });
                    vals[i] = m.Invoke(null, new object[] { args[i] });
                }
                i++;
            }
            return vals;
        }

        public CsvParser RemoveWith(string word)
        {
            for (int i = 0; i < lines.Count; i++)
            {
                if (lines[i].StartsWith(word))
                    lines.RemoveAt(i--);
                    
            }
            return this;
        }
        public CsvParser RemoveEvenIndexes()
        {
            for (int i = 0; i < lines.Count; i++)
            {
                lines.RemoveAt(i);

            }
            return this;
        }
        public CsvParser RemoveOddIndexes()
        {
            for (int i = 1; i < lines.Count; i++)
            {
                lines.RemoveAt(i);

            }
            return this;
        }

        public override string ToString()
        {
            return String.Join("\n", lines);
        }
    }
}
