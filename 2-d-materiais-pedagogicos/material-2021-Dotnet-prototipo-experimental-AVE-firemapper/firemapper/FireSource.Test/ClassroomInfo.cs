using FireMapper.Attributes;

namespace FireSource.Test
{
    [FireCollection("Classrooms")]
    public record ClassroomInfo([property: FireKey] string Token, string Teacher)
    {
        public ClassroomInfo() : this(null, null)
        {
        }
    }
}