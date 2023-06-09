using System;
using System.Collections.Generic;
using System.IO;
using Google.Cloud.Firestore;

namespace FireSource.Test
{
    /// <summary>
    /// A single test context shared among all the tests.
    /// Test classes should implement IClassFixture<FireStoreFixture> and
    /// provide a constructor to inject a Fixture object.
    /// </summary>
    public class FireStoreFixture : IDisposable
    {
        const string FIREBASE_PROJECT_ID = "dummydemo-11dd3";

        public readonly FireDataSource studentsDb = new FireDataSource(
                FIREBASE_PROJECT_ID,
                "Students", // Collection
                "Number",   // key field
                "Resources\\dummydemo-11dd3-firebase-adminsdk-vp6c5-28b7f0fa93.json"
            );
        public readonly FireDataSource classroomsDb = new FireDataSource(
                FIREBASE_PROJECT_ID,
                "Classrooms", // Collection
                "Token",   // key field
                "Resources\\dummydemo-11dd3-firebase-adminsdk-vp6c5-28b7f0fa93.json"
            );

        /// <summary>
        /// The key is the classroom token e.g. TLI41D and the value
        /// is the Id auto-generated by FireStore.
        /// </summary>
        public readonly List<string> classroomsIds = new List<string>();

        public void Dispose()
        {
            ///
            /// ... clean up test data from the database ...
            /// 
            IEnumerable<Dictionary<string, object>> students = studentsDb.GetAll();
            foreach(var pairs in students) 
            {
                studentsDb.Delete(pairs["Number"]);
            }
            foreach(var token in classroomsIds) 
            {
                classroomsDb.Delete(token);
            }
        }

        public FireStoreFixture()
        {
            CreateClassrooms();
            AddToFirestoreFrom("Resources\\isel-AVE-2021.txt");
        }
        void CreateClassrooms()
        {
            InsertClassroomFor("TLI41D", "Miguel Gamboa");
            InsertClassroomFor("TLI42D", "Luís Falcão");
            InsertClassroomFor("TLI41N", "Miguel Gamboa");
            InsertClassroomFor("TLI4NXST", "NA");
            InsertClassroomFor("TLI4DXST", "NA");
        }
        void InsertClassroomFor(string token, string teacher) {
            classroomsDb.Add(new Dictionary<string, object>() {
                {"Teacher", teacher},
                {"Token", token},
            });
            classroomsIds.Add(token);
        }

        void AddToFirestoreFrom(string path)
        {
            foreach (string line in Lines(path))
            {
                string[] words = line.Split(";");
                studentsDb.Add(new Dictionary<string, object>() {
                    {"Number", words[0]},
                    {"Name", words[1]},
                    {"Classroom", words[2]}, 
                });
            }
        }

        static IEnumerable<string> Lines(string path)
        {
            string line;
            IList<string> res = new List<string>();
            using(StreamReader file = new StreamReader(path)) // <=> try-with resources do Java >= 7
            {
                while ((line = file.ReadLine()) != null)
                {
                    res.Add(line);
                }
            }
            return res;
        }
    }
}