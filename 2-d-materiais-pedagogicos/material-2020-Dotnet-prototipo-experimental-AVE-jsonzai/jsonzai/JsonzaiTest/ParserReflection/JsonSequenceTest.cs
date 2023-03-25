using System;
using System.Text;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Jsonzai.Test.Model;

namespace Jsonzai.Test.ParserReflection {

    [TestClass]
    [DeploymentItem("ParserReflection\\Resources\\", "ResourcesReflection")]
    public class JsonSequenceTest {

        [TestMethod]
        public void TestSimpleIntArray() {
            string filename = "ResourcesReflection\\Seq_Simple.json";
            IEnumerable<int> e = JsonSequence.SequenceFrom<int>(filename);
            ValidateSimpleIntArray(e);
        }

        [TestMethod]
        public void TestSimpleIntArray_ShouldTrim() {
            string filename = "ResourcesReflection\\Seq_Simple_ShouldTrim.json";
            IEnumerable<int> e = JsonSequence.SequenceFrom<int>(filename);
            ValidateSimpleIntArray(e);
        }

        [TestMethod]
        public void TestStudentsArray() {
            string filename = "ResourcesReflection\\Seq_Students.json";
            IEnumerable<Student> e = JsonSequence.SequenceFrom<Student>(filename);
            ValidateAllStudents(e);
        }

        [TestMethod]
        public void TestStudentsArray_ShouldTrim() {
            string filename = "ResourcesReflection\\Seq_Students_ShouldTrim.json";
            IEnumerable<Student> e = JsonSequence.SequenceFrom<Student>(filename);
            ValidateAllStudents(e);
        }



        //////////////////////
        //// AUX METHODS /////
        //////////////////////

        private static void ValidateSimpleIntArray(IEnumerable<int> e) {
            IEnumerator<int> enumerator = e.GetEnumerator();
            for (int i = 1; i <= 5; ++i) {
                enumerator.MoveNext();
                Assert.AreEqual(enumerator.Current, i);
            }
        }

        private static void ValidateAllStudents(IEnumerable<Student> e) {
            IEnumerator<Student> enumerator = e.GetEnumerator();
            for (int i = 0; i < 3; ++i) {
                enumerator.MoveNext();
                Assert.IsNotNull(enumerator.Current.Name);
                Assert.AreEqual(enumerator.Current.Nr, i);
                Assert.AreEqual(enumerator.Current.Group, i+10);
                Assert.AreEqual(enumerator.Current.GithubId, "git"+i);
            }
        }
    }
}
