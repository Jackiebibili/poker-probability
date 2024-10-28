import java.util.*;
import java.util.concurrent.*;

public class Poker {
   public static final int MAX_CARD_NUMBER = 52;

   public OddsWrapper calcPlayerChance(List<Hands> playersHands) {
      RecursiveTask<OddsWrapper> allPlayersChanceTask = new CalculateAllPlayersWins(playersHands);
      ForkJoinPool pool = new ForkJoinPool();
      return pool.invoke(allPlayersChanceTask);
   }

   class CalculateAllPlayersWins extends RecursiveTask<OddsWrapper> {
      List<Hands> playersHands;
      List<RecursiveTask<int[][]>> tasks;

      public CalculateAllPlayersWins(List<Hands> playersHands) {
         tasks = new ArrayList<>();
         this.playersHands = new ArrayList<>(playersHands);
      }

      private void simulateDeck() {
         simulateHelper(getAllAvailableCards(playersHands), 0, new Card[VisibleCards.SIZE], 0);
      }

      private void simulateHelper(Card[] cards, int idx, Card[] visibleCards, int nextIdx) {
         if (nextIdx >= visibleCards.length) {
            RecursiveTask<int[][]> sub = new CalculateHoleCardsWinsFromCommunityCards(visibleCards);

            sub.fork();
            tasks.add(sub);
            return;
         }

         if (cards.length - idx < visibleCards.length - nextIdx) {
            return;
         }

         // pick or not pick
         visibleCards[nextIdx] = cards[idx];
         simulateHelper(cards, idx + 1, visibleCards, nextIdx + 1);

         simulateHelper(cards, idx + 1, visibleCards, nextIdx);
      }

      public OddsWrapper compute() {
         simulateDeck();

         int[] wins = new int[playersHands.size()];
         int[] tie = new int[playersHands.size()];

         int totalComb = tasks.size();
         for (RecursiveTask<int[][]> sub : tasks) {
            int[][] ret = sub.join();
            int[] subwins = ret[0], subtie = ret[1];

            for (int i = 0; i < playersHands.size(); i++) {
               wins[i] += subwins[i];
            }
            for (int i = 0; i < playersHands.size(); i++) {
               tie[i] += subtie[i];
            }
         }

         return new OddsWrapper(wins, tie, totalComb);
      }

      class CalculateHoleCardsWinsFromCommunityCards extends RecursiveTask<int[][]> {
         VisibleCards obj; // community cards

         public CalculateHoleCardsWinsFromCommunityCards() {
            obj = null;
         }

         public CalculateHoleCardsWinsFromCommunityCards(Card[] cards) {
            this.obj = new VisibleCards(cards);
         }

         public int[][] compute() {
            int[] wins = new int[playersHands.size()];
            int[] tie = new int[playersHands.size()];
            VisibleCards maxComb = null;
            List<Integer> maxPlayersIndices = new ArrayList<>();
            int len = playersHands.size();
            for (int i = 0; i < len; i++) {
               Hands playerHand = playersHands.get(i);
               VisibleCards bestPlayerComb = obj.getBestCombination(playerHand);

               int comp = 0;
               if (maxComb == null
                     || ((comp = RankComparator.compareCards(bestPlayerComb, maxComb)) < 0)) {
                  maxComb = bestPlayerComb;
                  maxPlayersIndices.clear();
                  maxPlayersIndices.add(i);
               } else if (comp == 0) {
                  maxPlayersIndices.add(i);
               }
            }

            if (maxPlayersIndices.size() > 1) {
               // tie
               for (int winningPlayerIdx : maxPlayersIndices) {
                  tie[winningPlayerIdx]++;
               }
            } else {
               // win
               for (int winningPlayerIdx : maxPlayersIndices) {
                  wins[winningPlayerIdx]++;
               }
            }

            return new int[][] {wins, tie};
         }
      }

      public HashSet<String> setIgnoreCards(List<Hands> playersHands) {
         HashSet<String> ignoreCards = new HashSet<>();
         // additional cards to ignore

         // default ignore cards
         for (Hands h : playersHands) {
            for (int i = 0; i < h.size(); i++) {
               Card card = h.getCardByIdx(i);
               ignoreCards.add(card.toString());
            }
         }

         return ignoreCards;
      }

      private Card[] getAllAvailableCards(List<Hands> playersHands) {
         HashSet<String> ignoreCards = setIgnoreCards(playersHands);

         List<Card> allCards = new ArrayList<>();
         for (int c = 0; c < MAX_CARD_NUMBER; c++) {
            int value = 1 + (c % Card.CARD_TOTAL_NUMBER_PER_SUITE);
            int suite = c / Card.CARD_TOTAL_NUMBER_PER_SUITE;

            Card card = new Card(value, suite);
            if (!ignoreCards.contains(card.toString())) {
               allCards.add(card);
            }
         }

         return allCards.toArray(new Card[0]);
      }
   }


   /*-
   public int[][] calcPlayerChanceToAll(Hands myHands) {
      RecursiveTask<int[][]> allPlayersChanceTask = new CalculateMyWinningChanceToAllPossibleOpponent(myHands);
      ForkJoinPool pool = new ForkJoinPool();
      return pool.invoke(allPlayersChanceTask);
   }
   
   class CalculateMyWinningChanceToAllPossibleOpponent extends RecursiveTask<int[][]> {
      Hands myHands;
      List<Hands> playersHands1;
      List<Hands> playersHands2;
   
      public CalculateMyWinningChanceToAllPossibleOpponent(Hands myHands) {
         this.myHands = myHands;
   
         playersHands1 = new ArrayList<>();
         playersHands1.add(new Hands(
               new Card(12, 0),
               new Card(12, 1)));
         playersHands1.add(new Hands(
               new Card(13, 0),
               new Card(13, 1)));
   
         playersHands2 = new ArrayList<>();
         playersHands2.add(new Hands(
               new Card(12, 0),
               new Card(12, 1)));
         playersHands2.add(new Hands(
               new Card(11, 0),
               new Card(11, 1)));
   
      }
   
      public int[][] compute() {
         RecursiveTask<int[][]> sub = new CalculateAllPlayersWins(playersHands1);
         sub.fork();
   
         CalculateAllPlayersWins sub2 = new CalculateAllPlayersWins(playersHands2);
         sub2.compute();
   
         sub.join();
   
         return null;
      }
   }
   */
}
