using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using FireSource.Test;
using Xunit;
using Xunit.Abstractions;

namespace FireMapper.Test
{
    [Collection("FireStoreFixture collection")]
    public class FireDataMapperTest
    {
        readonly ITestOutputHelper output;
        private readonly FireDataMapper studentsMapper;

        public FireDataMapperTest(ITestOutputHelper output, FireStoreFixture fix)
        {
            this.output = output;
            this.studentsMapper = new FireDataMapper(typeof(Student), fix.studentsDb.ProjectId, fix.studentsDb.CredentialsPath);
        }
        
        [Fact]
        public void GetAll()
        {
            int count = 0;
            foreach(var obj in studentsMapper.GetAll()) {
                output.WriteLine(obj.ToString());
                count++;
            }
            Assert.Equal(9, count);
        }
        [Fact]
        public void GetById()
        {
            Student st = (Student) studentsMapper.GetById("44999");
            Assert.Equal("Bartiskovley Navriska Bratsha Sverilev", st.Name);
        }
        [Fact]
        public void UpdateStudent()
        {
            Student st = (Student) studentsMapper.GetById("55999");
            Assert.Equal("Gama Danda Canda Lanta", st.Name);
            studentsMapper.Update(new Student("55999", "Nuwanda Dead Poets Society", st.Classroom));
            Student actual = (Student) studentsMapper.GetById("55999");
            Assert.Equal("Nuwanda Dead Poets Society", actual.Name);
        }


        [Fact]
        public void AddGetAndDeleteAndGetAgain()
        {
            ///
            /// Arrange and Insert new Student
            /// 
            Student st = new Student("823648", "Ze Manel", new ClassroomInfo("TLI41D", "Miguel Gamboa"));
            studentsMapper.Add(st);
            /// 
            /// Get newby Student
            /// 
            Student actual = (Student) studentsMapper.GetById(st.Number);
            Assert.Equal(st.Name, actual.Name);
            Assert.Equal(st.Number, actual.Number);
            Assert.Equal(st.Classroom, actual.Classroom); 
            /// 
            /// Remove Student
            /// 
            studentsMapper.Delete(st.Number);
            Assert.Null(studentsMapper.GetById(st.Number));
        }

    }
}
