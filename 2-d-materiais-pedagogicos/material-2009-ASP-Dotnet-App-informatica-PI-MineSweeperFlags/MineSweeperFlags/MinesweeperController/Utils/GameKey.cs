using System;

namespace MinesweeperControllers.Utils
{
    internal static class GameKey
    {
        static int count;

        static GameKey()
        {
            count = 0;
        }

        public static string GetKey()
        {

            //Nota - o formato tem que ter um g e 9 - {0} . utilizado no método getPos da class cell. Ficheiro Cell.js
            return string.Format( "g{0}", System.Threading.Interlocked.Increment(ref count).ToString("000000000") );
        }
    }
}
