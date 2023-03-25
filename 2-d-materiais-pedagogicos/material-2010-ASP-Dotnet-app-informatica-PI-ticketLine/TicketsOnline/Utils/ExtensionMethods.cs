using System.Text;

namespace TicketsOnline.Utils
{
    public static class ExtensionMethods
    {
        public static string AllElementsToString(this int[] array)
        {
            StringBuilder stringBuilder = new StringBuilder();

            int i = 0;
            for (; i < array.Length; i++)
            {
                stringBuilder.Append(array[i]);
                stringBuilder.Append(", ");
            }

            return stringBuilder.ToString(0, stringBuilder.Length - 2);
        }
    }
}