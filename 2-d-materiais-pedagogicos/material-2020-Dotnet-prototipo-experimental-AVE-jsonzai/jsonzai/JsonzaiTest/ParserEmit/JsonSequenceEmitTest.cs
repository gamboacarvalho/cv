using System;
using System.Text;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Jsonzai.Test.Model;

namespace Jsonzai.Test.ParserEmit {

    [TestClass]
    [DeploymentItem("ParserEmit\\Resources\\", "ResourcesEmit")]
    public class JsonSequenceEmitTest {

        [TestMethod]
        public void TestSimpleIntArray() {
            string filename = "ResourcesEmit\\Seq_Simple.json";
            IEnumerable<int> e = JsonSequence.SequenceEmitFrom<int>(filename);
            ValidateSimpleIntArray(e);
        }

        [TestMethod]
        public void TestSimpleIntArray_ShouldTrim() {
            string filename = "ResourcesEmit\\Seq_Simple_ShouldTrim.json";
            IEnumerable<int> e = JsonSequence.SequenceEmitFrom<int>(filename);
            ValidateSimpleIntArray(e);
        }

        [TestMethod]
        public void TestStudentsArray() {
            string filename = "ResourcesEmit\\Seq_Students.json";
            IEnumerable<Student> e = JsonSequence.SequenceEmitFrom<Student>(filename);
            ValidateAllStudents(e);
        }

        [TestMethod]
        public void TestStudentsArray_ShouldTrim() {
            string filename = "ResourcesEmit\\Seq_Students_ShouldTrim.json";
            IEnumerable<Student> e = JsonSequence.SequenceEmitFrom<Student>(filename);
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
