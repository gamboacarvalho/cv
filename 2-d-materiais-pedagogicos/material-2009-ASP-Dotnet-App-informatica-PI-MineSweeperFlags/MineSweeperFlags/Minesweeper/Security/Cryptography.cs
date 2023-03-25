using System;
using System.Text;
using System.Security.Cryptography;

namespace Minesweeper.Security
{
    internal class Cryptography
    {
        public static byte[] Encrypt(String pwd)
        {
            byte[] pwdhash = null;
            try
            {                
                using (MD5CryptoServiceProvider hashmd5 = new MD5CryptoServiceProvider())
                {
                    pwdhash = hashmd5.ComputeHash(ASCIIEncoding.ASCII.GetBytes(pwd));
                }
                
            }
            catch (Exception e){ throw e; }
            return pwdhash;
        }
    }
}
