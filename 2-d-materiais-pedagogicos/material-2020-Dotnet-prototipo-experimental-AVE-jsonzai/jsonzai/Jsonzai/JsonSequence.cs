using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Jsonzai.Tokens;

namespace Jsonzai {

    public class JsonSequence {

        /**
         *  Current know supported encodings: UTF-8
         */
        public static IEnumerable<T> SequenceFrom<T>(string filename) {
            JsonTokensFromFile tokens = new JsonTokensFromFile(filename);
            tokens.Pop(JsonTokensFromFile.ARRAY_OPEN);
            tokens.Trim();

            Type t = typeof(T);

            do {
                yield return (T)JsonParser.Parse(tokens, t);
                tokens.Trim();
            } while (tokens.HasNext(BaseJsonTokens.ARRAY_END));
        }

        public static IEnumerable<T> SequenceEmitFrom<T>(string filename) {
            JsonTokensFromFile tokens = new JsonTokensFromFile(filename);
            tokens.Pop(JsonTokensFromFile.ARRAY_OPEN);
            tokens.Trim();

            Type t = typeof(T);

            do {
                yield return (T)JsonParserEmit.Parse(tokens, t);
                tokens.Trim();
            } while (tokens.HasNext(BaseJsonTokens.ARRAY_END));
        }
    }
}
