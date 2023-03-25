using System;
using System.IO;

namespace Jsonzai.Tokens {
    public class JsonTokensFromFile : BaseJsonTokens {

        private readonly FileStream src;
        private char curr;
        public override char Current => curr;

        public JsonTokensFromFile(string filename) {
            this.src = new FileStream(filename, FileMode.Open);
            MoveNext();
            if (!IsArray())
                throw new FormatException("Root Element of file is not an array");
        }

        public override bool MoveNext() {
            int c = src.ReadByte();
            if (c == 0)
                return false;
            curr = (char)c;
            return true;
        }

        public override void Trim() {
            while (Current == ' ' || Current == '\n' || Current == '\r' || Current == '\t') MoveNext();
        }

        public override char Pop() {
            char token = Current;
            MoveNext();
            return token;
        }

        public override void Pop(char expected) {
            if (Current != expected)
                throw new InvalidOperationException("Expected " + expected + " but found " + Current);
            MoveNext();
        }

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

        public bool IsArray() {
            return Current == ARRAY_OPEN;
        }

        public void Close() {
            src.Close();
        }
    }
}
