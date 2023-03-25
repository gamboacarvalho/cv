using FireMapper.Attributes;

namespace FireSource.Test
{
     [FireCollection("Students")]
    public record Student(
        [property:FireKey] string Number,
        string Name, 
        ClassroomInfo Classroom) 
    {
        public Student() :this(null, null, null) {}
     }
}