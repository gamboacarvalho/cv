using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.Linq;
using System.Data.Linq.Mapping;

namespace MinesweeperForum
{
    [Table(Name = "Thread")]
    public class Thread
    {
        [Column(IsPrimaryKey = true, IsDbGenerated = true)]
        public int Id { get; private set; }
        [Column]
        public string Publisher { get; set; }
        [Column]
        public string Title { get; set; }
        [Column]
        public int Visits { get; set; }
        [Column]
        public DateTime AddDate { get; set; }
    }

}
