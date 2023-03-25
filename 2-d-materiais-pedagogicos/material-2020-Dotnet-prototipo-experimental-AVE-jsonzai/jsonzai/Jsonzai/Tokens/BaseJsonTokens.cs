using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Jsonzai.Tokens {

    public abstract class BaseJsonTokens {
        public const char OBJECT_OPEN = '{';
        public const char OBJECT_END = '}';
        public const char ARRAY_OPEN = '[';
        public const char ARRAY_END = ']';
        public const char DOUBLE_QUOTES = '"';
        public const char COMMA = ',';
        public const char COLON = ':';

        public abstract char Current { get; }

        public abstract bool MoveNext();

        public abstract void Trim();

        public abstract char Pop();

        public abstract void Pop(char expected);

        public abstract string PopWordFinishedWith(char delimiter);

        public abstract string PopWordPrimitive();

        public bool IsEnd(char curr) {
            return curr == OBJECT_END || curr == ARRAY_END || curr == COMMA;
        }

        public bool HasNext(char end) {
            if (Current != end) {
                Pop(BaseJsonTokens.COMMA);
                Trim();
                return true;
            }
            return false;
        }
    }
}
