using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Jsonzai.Test.Model;
using Newtonsoft.Json;

namespace Jsonzai.Test.ParserReflection
{

    [TestClass]
    public class JsonParserTest {
        
        [TestMethod]
        public void TestIntArray() {
            string src = "[1, 2, 3]";
            int[] arr = JsonParser.Parse<int[]>(src);
            Assert.AreEqual(arr[0], 1);
            Assert.AreEqual(arr[1], 2);
            Assert.AreEqual(arr[2], 3);
        }

        [TestMethod]
        public void TestParsingStudents() {
            string src = "{\"Name\": \"Ze Manel\", \"Nr\": 6512, \"Group\": 11, \"GithubId\": \"omaior\"}";
            Student std = JsonParser.Parse<Student>(src);
            Assert.AreEqual("Ze Manel", std.Name);
            Assert.AreEqual(6512, std.Nr);
            Assert.AreEqual(11, std.Group);
            Assert.AreEqual("omaior", std.GithubId);
        }

        [TestMethod]
        public void TestSiblings() {
            string src = "{\"Name\": \"Ze Manel\", \"Sibling\": { \"Name\": \"Maria Papoila\", \"Sibling\": { \"Name\": \"Kata Badala\"}}}";
            Person p = JsonParser.Parse<Person>(src);
            Assert.AreEqual("Ze Manel", p.Name);
            Assert.AreEqual("Maria Papoila", p.Sibling.Name);
            Assert.AreEqual("Kata Badala", p.Sibling.Sibling.Name);
        }

        [TestMethod]
        public void TestParsingPersonWithBirth() {
            string src = "{\"Name\": \"Ze Manel\", \"Birth\": {\"Year\": 1999, \"Month\": 12, \"Day\": 31}}";
            Person p = JsonParser.Parse<Person>(src);
            Assert.AreEqual("Ze Manel", p.Name);
            Assert.AreEqual(1999, p.Birth.Year);
            Assert.AreEqual(12, p.Birth.Month);
            Assert.AreEqual(31, p.Birth.Day);
        }

        [TestMethod]
        public void TestParsingPersonArray() {
            string src = "[{\"Name\": \"Ze Manel\"}, {\"Name\": \"Candida Raimunda\"}, {\"Name\": \"Kata Mandala\"}]";
            Person [] ps = JsonParser.Parse<Person[]>(src);
            Assert.AreEqual(3, ps.Length);
            Assert.AreEqual("Ze Manel", ps[0].Name);
            Assert.AreEqual("Candida Raimunda", ps[1].Name);
            Assert.AreEqual("Kata Mandala", ps[2].Name);
        }

        [TestMethod]
        public void TestParsingClassroom() {
            string src = "{\"Id\": 31, \"Students\": [{\"Name\": \"Ze Manel\", \"Nr\": 6512, \"Group\": 11, \"GithubId\": \"omaior\"}, {\"Name\": \"Candida Raimunda\", \"Nr\": 3344, \"Group\": 6, \"GithubId\": \"beleza\"}]}";
            Classroom cl = JsonParser.Parse<Classroom>(src);
            Assert.AreEqual(31, cl.Id);
            Assert.AreEqual(2, cl.Students.Length);
            Assert.AreEqual("Ze Manel", cl.Students[0].Name);
            Assert.AreEqual("Candida Raimunda", cl.Students[1].Name);
        }

        [TestMethod]
        public void TestParsingSerializedClassroom() {
            Student[] students = new Student[]
            {
                new Student(6512, "Ze Manel", 11, "omaior"),
                new Student(3344, "Candida Raimunda", 6, "beleza")
            };

            Classroom cl1 = new Classroom(31, students);
            string src = JsonConvert.SerializeObject(cl1);
            Classroom cl2 = JsonParser.Parse<Classroom>(src);

            Assert.AreEqual(cl1.Id, cl2.Id);
            Assert.AreEqual(cl1.Students.Length, cl2.Students.Length);
            Assert.AreEqual(cl1.Students[0].Name, cl2.Students[0].Name);
            Assert.AreEqual(cl1.Students[1].Name, cl2.Students[1].Name);
        }

        [TestMethod]
        public void TestParsingSerializedAccount() {
            double[] transactions = new double[]
            {
                100,
                -20,
                500,
                -250,
                -50
            };

            Account acc = new Account(280, transactions);
            string src = JsonConvert.SerializeObject(acc);
            Account acc2 = JsonParser.Parse<Account>(src);

            Assert.AreEqual(acc.Balance, acc2.Balance);
            Assert.AreEqual(acc.Transactions.Length, acc2.Transactions.Length);
            Assert.AreEqual(acc.Transactions[0], acc2.Transactions[0]);
            Assert.AreEqual(acc.Transactions[3], acc2.Transactions[3]);
        }

        [TestMethod]
        public void TestParsingSerializedRgbColor() {
            RgbColor color = new RgbColor(250, 100, 50);
            string src = JsonConvert.SerializeObject(color);
            RgbColor color2 = JsonParser.Parse<RgbColor>(src);

            Assert.AreEqual(color.HexCode, color2.HexCode);
            Assert.AreEqual(color.Red, color2.Red);
            Assert.AreEqual(color.Green, color2.Green);
            Assert.AreEqual(color.Blue, color2.Blue);
        }

        [TestMethod]
        public void TestParsingRgbColorAnnotatedUsingJsonPropertyAttribute() {
            string src = "{\"First\": 255, \"Second\": 100, \"Third\": 50}";
            RgbColorAnnotated color = JsonParser.Parse<RgbColorAnnotated>(src);
            Assert.AreEqual(255, color.Red);
            Assert.AreEqual(100, color.Green);
            Assert.AreEqual(50,  color.Blue);
        }

        [TestMethod]
        public void TestParsingWorkUsingJsonConvertAttribute() {
            string src = "{\"Id\": 666, \"Desc\": \"Kill the Beast\", \"DTime\": \"1982-03-22\"}";
            Work work = JsonParser.Parse<Work>(src);
            Assert.AreEqual(666, work.Id);
            Assert.AreEqual("Kill the Beast", work.Desc);
            Assert.AreEqual(new DateTime(1982, 3, 22), work.DTime);
        }

        [TestMethod]
        public void TestParsingWorkUsingJsonConvertAndJsonPropertyAttribute() {
            string src = "{\"Id\": 666, \"Desc\": \"Kill the Beast\", \"DTime\": \"1982-03-22\", \"OldDate\": \"1980-04-14\"}";
            Work work = JsonParser.Parse<Work>(src);
            Assert.AreEqual(666, work.Id);
            Assert.AreEqual("Kill the Beast", work.Desc);
            Assert.AreEqual(new DateTime(1982, 3, 22), work.DTime);
            Assert.AreEqual(new DateTime(1980, 4, 14), work.DTime2);
        }

        [TestMethod]
        public void TestParsingGitHubProfileUsingJsonConvertAttribute()
        {
            string src = "{\"Id\": \"Antossouras\", \"Name\": \"António Vassouras\", \"BirthDate\": \"1914-12-25\", \"Uri\": \"https://github.com/Antossouras\"}";
            GitHubProfile profile = JsonParser.Parse<GitHubProfile>(src);
            Assert.AreEqual("Antossouras", profile.Id);
            Assert.AreEqual("António Vassouras", profile.Name);
            Assert.AreEqual(new DateTime(1914, 12, 25), profile.BirthDate);
            Assert.AreEqual(new Uri("https://github.com/Antossouras"), profile.Uri);
        }

        [TestMethod]
        public void TestParsingSerializedGitHubProfileUsingJsonConvertAttribute() {
            GitHubProfile profile1 = new GitHubProfile("MariaF", "Maria Fernandes", new DateTime(1969, 10, 01), new Uri("https://github.com/MariaF"));
            string src = JsonConvert.SerializeObject(profile1);
            GitHubProfile profile2 = JsonParser.Parse<GitHubProfile>(src);

            Assert.AreEqual(profile1.Id, profile2.Id);
            Assert.AreEqual(profile1.Name, profile2.Name);
            Assert.AreEqual(profile1.BirthDate, profile2.BirthDate);
            Assert.AreEqual(profile1.Uri, profile2.Uri);
        }

        [TestMethod]
        [ExpectedException(typeof(IndexOutOfRangeException))]
        public void TestBadJsonObjectWithUnclosedBrackets() {
            string src = "{\"Name\": \"Ze Manel\", \"Sibling\": { \"Name\": \"Maria Papoila\", \"Sibling\": { \"Name\": \"Kata Badala\"}";
            Person p = JsonParser.Parse<Person>(src);
        }

        [TestMethod]
        [ExpectedException(typeof(InvalidOperationException))]
        public void TestBadJsonObjectWithWrongCloseToken() {
            string src = "{\"Name\": \"Ze Manel\", \"Sibling\": { \"Name\": \"Maria Papoila\"]]";
            Person p = JsonParser.Parse<Person>(src);
        }

        [TestMethod]
        [ExpectedException(typeof(NullReferenceException))]
        public void TestBadJsonObjectWithWrongArrayFormat() {
            string src = "[4, 5, [1, 2, 3], 2]";
            int[] arr = JsonParser.Parse<int[]>(src);
        }
    }
}
