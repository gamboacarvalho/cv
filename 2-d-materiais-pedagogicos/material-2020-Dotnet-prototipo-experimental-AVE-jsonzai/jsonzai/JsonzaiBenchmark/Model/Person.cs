namespace JsonzaiBenchmark.Test.Model {
    public class Person {
        public string Name { get; set; }
        public Date Birth { get; set; }
        public Person Sibling { get; set; }

        public Person() {
        }

        public Person(string name) {
            this.Name = name;
        }
    }
}
