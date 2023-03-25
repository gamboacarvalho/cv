using System;

namespace Jsonzai.Tokens {
    public class JsonTokens : BaseJsonTokens {

        private readonly char[] src;
        private int index;

        public JsonTokens(string src) {
            this.src = src.ToCharArray();
            this.index = 0;
        }

        public override char Current => src[index];

        public override bool MoveNext() {
            index++;
            return index == src.Length ? false : true;
        }

        public override void Trim() {
            while (src[index] == ' ' || src[index] == '\n' || src[index] == '\r' || src[index] == '\t') MoveNext();
        }

        public override char Pop() {
            char token = src[index];
            index++;
            return token;
        }

        public override void Pop(char expected) {
            if (Current != expected)
                throw new InvalidOperationException("Expected " + expected + " but found " + Current);
            index++;
        }

        /// <summary>
        /// Consumes all characters until find delimiter and accumulates into a string.
        /// </summary>
        /// <param name="delimiter">May be one of DOUBLE_QUOTES, COLON or COMA</param>
        public override string PopWordFinishedWith(char delimiter) {
            Trim();
            string acc = "";
            for (; Current != delimiter; MoveNext()) {
                acc += Current;
            }
            MoveNext(); // Discard delimiter
            Trim();
            return acc;
        }

        public override string PopWordPrimitive() {
            Trim();
            string acc = "";
            for (; !IsEnd(Current); MoveNext()) {
                acc += Current;
            }
            Trim();
            return acc;
        }
    }
}
