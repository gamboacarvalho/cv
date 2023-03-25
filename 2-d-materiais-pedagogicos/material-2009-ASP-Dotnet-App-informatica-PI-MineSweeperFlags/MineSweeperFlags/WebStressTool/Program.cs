using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;

namespace WebStressTool
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            try
            {
                Application.EnableVisualStyles();
                Application.SetCompatibleTextRenderingDefault(false);
                Application.Run(new FormStressTool());
            }
            catch (Exception exc) { Console.WriteLine( exc.Message ); }
        }
    }
}
