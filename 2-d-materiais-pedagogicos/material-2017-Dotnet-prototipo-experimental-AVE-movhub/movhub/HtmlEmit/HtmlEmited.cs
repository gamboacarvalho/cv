using System;
using System.Collections.Generic;

namespace HtmlEmit
{

    public class HtmlEmited
    {

        static Dictionary<Type, IHtmlParser> dictionary = new Dictionary<Type, IHtmlParser>();

        public string ToHtml(object obj)
        {
            IHtmlParser ig;
            Type type = obj.GetType();
            if (!dictionary.TryGetValue(type, out ig)) {
                ig =Emit.EmitDetails(type);
                dictionary.Add(type, ig); 
            }
            return ig.ParseToHtml(obj);
        }


        public string ToHtml<T>(IEnumerable<T> arr)
        {
            IHtmlParser ig;
            Type type = typeof(IEnumerable<T>);
            if (!dictionary.TryGetValue(type, out ig))
            {
                ig = Emit.EmitTable<T>(arr.GetType().GetElementType());
                dictionary.Add(type, ig);
            }
            return ig.ParseToHtml(arr);
        }
         
        
        public void ForTypeDetails<T>(Func<T, string> transf)
        {
            if (dictionary.ContainsKey(typeof(T)))
            {
                dictionary.Remove(typeof(T));
            }
            dictionary.Add(typeof(T), new ForTypeDetails<T>(transf));
        }

        public void ForTypeInTable<T>(IEnumerable<string> headers, Func<T, string> transf)
        {
            if (dictionary.ContainsKey(typeof(IEnumerable<T>)))
            {
                dictionary.Remove(typeof(IEnumerable<T>));
            }
            dictionary.Add(typeof(IEnumerable<T>), new ForTypeInTable<T>(headers, transf));
           
        }

        public void ForSequenceOf<T>(Func<IEnumerable<T>, string> transf)
        {
            IHtmlParser ig = new ForSequenceOf<T>(transf);
            if (dictionary.ContainsKey(typeof(T)))
            {
                dictionary.Remove(typeof(T));
            }
            dictionary.Add(typeof(T), ig);

            if (dictionary.ContainsKey(typeof(IEnumerable<T>)))
            {
                dictionary.Remove(typeof(IEnumerable<T>));
            }
            dictionary.Add(typeof(IEnumerable<T>), ig);
        }
    }
}