using System;
using JsonExSerializer;

namespace MinesweeperControllers.Utils
{
    internal class JSon
    {
        JSon() { }

        public static string Serialize<T>(T obj)
        {
            JsonExSerializer.Serializer serializer = new JsonExSerializer.Serializer(typeof(T));
            serializer.Context.IsCompact = true;
            serializer.Context.OutputTypeComment = false;
            serializer.Context.OutputTypeInformation = false;
            serializer.Context.SetJsonStrictOptions();

            return serializer.Serialize(obj);
        }
    }
}
