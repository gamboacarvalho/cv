namespace Jsonzai.Test.Model {
    public class Account {
        public double Balance { get; set; }
        public double[] Transactions { get; set; }

        public Account() {
        }

        public Account(double balance, double[] transactions) {
            Balance = balance;
            Transactions = transactions;
        }
    }
}