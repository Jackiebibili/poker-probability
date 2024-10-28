import java.util.*;

public class VisibleCards implements Cloneable {
   public static final int SIZE = 5;
   private Card[] cards;
   private VisibleCards _curMaxCards;

   public VisibleCards() {
      cards = new Card[SIZE];
   }

   public VisibleCards(Card c1, Card c2, Card c3, Card c4, Card c5) {
      cards = new Card[] {c1, c2, c3, c4, c5};
   }

   public VisibleCards(Card[] cards) {
      this();
      for (int i = 0; i < SIZE; i++) {
         this.cards[i] = cards[i];
      }
   }

   public VisibleCards getBestCombination(Hands playerHands) {
      this._curMaxCards = null;

      Card[] allCards = new Card[SIZE + playerHands.size()];
      Card[] visibleCards = new Card[SIZE];
      int idx = 0;
      for (int i = 0; i < SIZE; i++) {
         allCards[idx++] = this.cards[i];
      }
      for (int i = 0; i < playerHands.size(); i++) {
         allCards[idx++] = playerHands.getCardByIdx(i);
      }

      getBestCombinationHelper(allCards, 0, visibleCards, 0);

      return this._curMaxCards;
   }

   private void getBestCombinationHelper(Card[] cards, int idx, Card[] arr, int nextIdx) {
      if (nextIdx >= arr.length) {
         VisibleCards obj = new VisibleCards(arr);
         if (this._curMaxCards == null || RankComparator.compareCards(obj, this._curMaxCards) < 0) {
            this._curMaxCards = obj;
         }
         return;
      }

      if (cards.length - idx < arr.length - nextIdx) {
         return;
      }

      // pick or not pick
      arr[nextIdx] = cards[idx];
      getBestCombinationHelper(cards, idx + 1, arr, nextIdx + 1);

      getBestCombinationHelper(cards, idx + 1, arr, nextIdx);
   }

   public Card getCardByIdx(int idx) {
      if (idx < 0 || idx >= SIZE) {
         return null;
      }

      return cards[idx];
   }

   int size() {
      return SIZE;
   }

   public VisibleCards clone() {
      VisibleCards newObj = new VisibleCards();
      for (int i = 0; i < SIZE; i++) {
         newObj.cards[i] = cards[i];
      }

      return newObj;
   }

   public void sortCards() {
      Arrays.sort(cards);
   }

   public String toString() {
      String s = "";
      for (int i = 0; i < SIZE; i++) {
         s += " " + cards[i].value;
      }
      return s + "\n";
   }
}
