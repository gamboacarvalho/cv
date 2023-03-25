using System;
using System.Collections;
using System.Collections.Generic;

using Jsonzai.Converters;
using Jsonzai.Cache;
using Jsonzai.Cache.Setters;
using Jsonzai.Tokens;

namespace Jsonzai {

    public class JsonParserEmit {

        static readonly Type[] PARSE_ARGUMENTS_TYPES = { typeof(string) };
        private static Dictionary<Type, KlassInfoEmit> klassInfoDic = new Dictionary<Type, KlassInfoEmit>();

        private static KlassInfoEmit GetKlassInfo(Type key) {
            if (!klassInfoDic.TryGetValue(key, out KlassInfoEmit klass)) {
                klass = new KlassInfoEmit(key);
                klassInfoDic.Add(key, klass);
            }
            return klass;
        }

        public static object Parse(String source, Type klass) {
            return Parse(new JsonTokens(source), klass);
        }

        public static T Parse<T>(String source) {
            return (T)Parse(new JsonTokens(source), typeof(T));
        }

        public static object Parse(BaseJsonTokens tokens, Type klass) {
            switch (tokens.Current) {
                case BaseJsonTokens.OBJECT_OPEN:
                    return ParseObject(tokens, klass);
                case BaseJsonTokens.ARRAY_OPEN:
                    return ParseArray(tokens, klass);
                case BaseJsonTokens.DOUBLE_QUOTES:
                    return ParseString(tokens);
                default:
                    return ParsePrimitive(tokens, klass);
            }
        }

        private static string ParseString(BaseJsonTokens tokens) {
            tokens.Pop(BaseJsonTokens.DOUBLE_QUOTES); // Discard double quotes "
            return tokens.PopWordFinishedWith(BaseJsonTokens.DOUBLE_QUOTES);
        }

        private static object ParsePrimitive(BaseJsonTokens tokens, Type klass) {
            string word = tokens.PopWordPrimitive();
            if (!klass.IsPrimitive || typeof(string).IsAssignableFrom(klass))
                if (word.ToLower().Equals("null"))
                    return null;
                else
                    throw new InvalidOperationException("Looking for a primitive but requires instance of " + klass);

            // Invoke the corresponding Parse method of klass
            // NOTE: Culture dependent, decimal spacer requires to be set to "." in windows region settings.
            return klass.GetMethod("Parse", PARSE_ARGUMENTS_TYPES).Invoke(null, new object[] { word });
        }

        private static object ParseObject(BaseJsonTokens tokens, Type klass) {
            tokens.Pop(BaseJsonTokens.OBJECT_OPEN); // Discard bracket { OBJECT_OPEN
            object target = Activator.CreateInstance(klass);
            return FillObject(tokens, target);
        }

        private static object FillObject(BaseJsonTokens tokens, object target) {
            Type type = target.GetType();
            KlassInfoEmit klassInfo = GetKlassInfo(type);
            BaseSetter setter;
            string key;

            tokens.Trim();
            while (tokens.Current != BaseJsonTokens.OBJECT_END) {
                key = ParseString(tokens);
                tokens.Pop(BaseJsonTokens.COLON);
                tokens.Trim();

                setter = klassInfo.GetSetter(key);
                object value = Parse(tokens, setter.PropInfo.PropertyType);
                target = setter.SetValue(target, value);

                tokens.HasNext(BaseJsonTokens.OBJECT_END);
            }
            tokens.Pop(BaseJsonTokens.OBJECT_END); // Discard bracket } OBJECT_END
            return target;
        }

        private static object ParseArray(BaseJsonTokens tokens, Type klass) {
            ArrayList list = new ArrayList();
            tokens.Pop(BaseJsonTokens.ARRAY_OPEN); // Discard square brackets [ ARRAY_OPEN
            Type elemType = klass.GetElementType();
            while (tokens.Current != BaseJsonTokens.ARRAY_END) {
                list.Add(Parse(tokens, elemType));
                tokens.HasNext(BaseJsonTokens.ARRAY_END);
            }
            tokens.Pop(BaseJsonTokens.ARRAY_END); // Discard square bracket ] ARRAY_END
            return list.ToArray(elemType);
        }

        public static void AddConverter<T, R>(string pname, Func<string, R> converter) {
            IConverter conv = new Converter<R>(converter);
            KlassInfo klassInfo = GetKlassInfo(typeof(T));
            klassInfo.converters.Add(pname, conv);
        }

        public static void ClearConfiguration() {
            foreach (KlassInfoEmit klass in klassInfoDic.Values) {
                klass.setters.Clear();
                klass.converters.Clear();
            }
        }
    }
}
