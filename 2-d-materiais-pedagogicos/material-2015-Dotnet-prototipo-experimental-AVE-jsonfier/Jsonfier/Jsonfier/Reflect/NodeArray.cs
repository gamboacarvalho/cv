using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Jsonzai.Reflect
{
    class NodeArray : Node
    {
        private const string BEGIN = "[";
        private const string END = "]";

        public NodeArray(Reflector reflector, object src)
            : base(src, reflector)
        {
            if (!src.GetType().IsArray) throw new InvalidOperationException("src Object argument must be an Array!");
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
            return builder.Append(Jsonfier.ToJson(val, reflector));
        }

    }
}
