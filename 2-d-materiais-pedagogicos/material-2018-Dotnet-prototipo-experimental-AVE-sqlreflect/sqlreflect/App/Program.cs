using SqlReflect;
using SqlReflectTest.DataMappers;
using SqlReflectTest.Model;
using System;
using System.Collections;
using System.Diagnostics;

namespace App
{
    class Program
    {
        static readonly string connStr = @"
                    Server=(LocalDB)\MSSQLLocalDB;
                    Integrated Security=true;
                    AttachDbFileName=" +
                        Environment.CurrentDirectory +
                        "\\data\\NORTHWND.MDF";

        static void Main(string[] args)
        {
            CompareMappers<int, Employee>();
            CompareMappers<string, Customer>();

        }
        private static void CompareMappers<K, V>() { 
            Console.WriteLine("############## Reflect WITH Cache");
            IDataMapper emps = new ReflectDataMapper<K,V>(typeof(V), connStr, true);
            for (int i = 0; i < 5; i++)
            {
                GetAllItens(emps);
            }

            Console.WriteLine("############## Emit WITH Cache");
            emps = EmitDataMapper.Build(typeof(V), connStr, true);
            for (int i = 0; i < 5; i++)
            {
                GetAllItens(emps);
            }

            Console.WriteLine("############## Reflect NO Cache");
            emps = new ReflectDataMapper<K,V>(typeof(V), connStr, false);
            for (int i = 0; i < 5; i++)
            {
                GetAllItens(emps);
            }
        }

        private static void GetAllItens(IDataMapper data)
        {
            Stopwatch stopwatch = new Stopwatch();
            stopwatch.Start();
            IEnumerable res = data.GetAll();
            stopwatch.Stop();
            Console.WriteLine("Time elapsed (us): {0}", stopwatch.Elapsed.TotalMilliseconds * 1000);
        }
    }
}
