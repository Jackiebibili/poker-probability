public class OddsWrapper {
   int[] wins;
   int[] ties;
   int total;

   public OddsWrapper() {
      this(null, null, 0);
   }

   public OddsWrapper(int[] wins, int[] ties, int total) {
      this.wins = wins;
      this.ties = ties;
      this.total = total;
   }
}
