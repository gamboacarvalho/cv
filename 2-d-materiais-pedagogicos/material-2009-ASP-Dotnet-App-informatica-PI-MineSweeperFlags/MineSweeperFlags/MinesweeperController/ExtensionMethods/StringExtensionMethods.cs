using System;
using System.Text.RegularExpressions; 

namespace MinesweeperControllers.ExtensionMethods
{
    public static class StringExtensionMethods
    {

        public static bool IsEMail(this string instance)
        {
            string strRegex = @"^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}" +
                        @"\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\" +
                        @".)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$";

            return new Regex(strRegex).IsMatch(instance);

        }

    }
}
