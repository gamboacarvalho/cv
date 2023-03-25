using System;
using System.IO;

namespace Minesweeper
{
    public class Photo
    {
        public string Name { get; set; }

        public string ContentType {  get; set; }

        public Stream Image { get; set; }
    }
}