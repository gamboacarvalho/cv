using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Jsonzai.Reflect
{
    class NodeObject : Node
    {
        private const string BEGIN = "{";
        private const string END = "}";
        private const string COLON = ":";

        public NodeObject(object src, Reflector reflector)
            : base(src, reflector)
        {
        }

        protected sealed override string Begin
        {
            get
            {
                return BEGIN;
            }
        }

        protected sealed override string End
        {
            get
            {
                return END;
            }
        }

        protected override StringBuilder Stringify(string label, object val, StringBuilder builder, Reflector reflector)
        {
            builder.Append('"');
            builder.Append(label);
            builder.Append('"');
            builder.Append(COLON);
            builder.Append(Jsonfier.ToJson(val, reflector));
            return builder;
        }
    }
}

