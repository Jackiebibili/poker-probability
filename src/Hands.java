public class Hands {
   private Card[] myHands;
   public static final int SIZE = 2;

   public Hands() {
      myHands = new Card[SIZE];
   }

   public Hands(Card c1, Card c2) {
      myHands = new Card[] { c1, c2 };
   }

   public int size() {
      return SIZE;
   }

   public Card getCardByIdx(int idx) {
      if(idx < 0 || idx >= SIZE) {
         return null;
      }

      return myHands[idx];
   }

   public String toString() {
      String s = "";

      for(Card card : myHands) {
         s += card.toString() + "//";
      }

      return s;
   }
}
