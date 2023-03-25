namespace Jsonzai.Test.Model {
    public class Classroom {
        public int Id { get; set; }
        public Student[] Students { get; set; }

        public Classroom() {
        }

        public Classroom(int id, Student[] students) {
            Id = id;
            Students = students;
        }
    }
}