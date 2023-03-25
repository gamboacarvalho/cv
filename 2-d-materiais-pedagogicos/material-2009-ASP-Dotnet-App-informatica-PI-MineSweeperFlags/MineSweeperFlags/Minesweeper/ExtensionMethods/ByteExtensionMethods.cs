using System;

namespace Minesweeper.ExtensionMethods
{
    public static class ByteExtensionMethods
    {
        public static bool EqualsTo(this byte[] instance, byte[] arg1)
        {
            if (instance == null && arg1 == null) return true;

            if (instance == null || arg1 == null) return false;
            if (instance.Length != arg1.Length) return false;

            for (int i = 0; i < instance.Length; i++)
                if (instance[i] != arg1[i]) return false;

            return true;
        }
    }
}
