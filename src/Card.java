public class Card implements Comparable<Card> {
   int value; // 1-10, J, Q, K
   int suite; // 0-3

   public static final int ACE = 1;
   public static final int KING = 13;
   public static final int QUEEN = 12;
   public static final int JACK = 11;
   public static final int TEN = 10;

   public static final int LOW_SUITE = 0;
   public static final int HIGH_SUITE = 3;
   public static final int CLUB = 0;
   public static final int DIAMOUND = 1;
   public static final int HEART = 2;
   public static final int SPADE = 3;

   public static final int CARD_TOTAL_NUMBER_PER_SUITE = 13;
   public static final int NUMBER_OF_SUITE = 4;

   public Card(int cardOrderNumber) {
      // 1-10, J, Q, K
      value = 1 + ((cardOrderNumber - 1) / NUMBER_OF_SUITE);
      suite = cardOrderNumber % NUMBER_OF_SUITE;
   }

   public Card(int value, int suite) {
      this.value = value;
      this.suite = suite;
   }

   public static int compareCardValues(int cardValue1, int cardValue2) {
      if (cardValue1 == cardValue2) {
         return 0;
      } else if (cardValue1 == ACE) {
         return -1;
      } else if (cardValue2 == ACE) {
         return 1;
      } else {
         return cardValue2 - cardValue1;
      }
   }

   public int compareTo(Card other) {
      return compareCardValues(this.value, other.value);
   }

   public static int getNextLargerValue(int start, boolean treatAceAsOne) {
      if (start > KING || (!treatAceAsOne && start < ACE) || (treatAceAsOne && start <= ACE)) {
         return -1;
      }

      if (start == ACE && !treatAceAsOne) {
         return KING;
      }

      return start - 1;
   }

   public String toString() {
      return value + "_" + suite;
   }
}
