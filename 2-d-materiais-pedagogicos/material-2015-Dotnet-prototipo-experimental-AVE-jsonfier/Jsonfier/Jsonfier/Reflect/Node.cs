using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Jsonzai.Reflect
{
    abstract class Node
    {
        private const string COMMA = ",";

        private StringBuilder builder;
        public Node(object src, Reflector reflector)
        {
            builder = new StringBuilder();
            builder.Append(Begin);
            builder = reflector
                .ValuesFrom(src)
                .Aggregate(
                    builder,
                    (prev, curr) => AppendNode(curr, prev, reflector));
            builder.Remove(builder.Length - 1, 1); // Removes last comma
            builder.Append(End);
        }

        private StringBuilder AppendNode(KeyValuePair<string, object> node, StringBuilder builder, Reflector reflector) {
            builder = Stringify(node.Key, node.Value, builder, reflector); // key is null for array elements
            builder.Append(COMMA);
            return builder;
        }

        protected abstract string Begin { get; }

        protected abstract string End { get; }

        protected abstract StringBuilder Stringify(string label, object val, StringBuilder builder, Reflector reflector);



        internal string Stringify()
        {
            return builder.ToString();
        }
    }
}
