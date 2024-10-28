import java.util.*;

public class RankComparator {
   public static final int ROYAL_FLUSH_RANK = 1;
   public static final int STRAIGHT_FLUSH_RANK = 2;
   public static final int FOUR_OF_A_KIND_RANK = 3;
   public static final int FULL_HOUSE_RANK = 4;
   public static final int FLUSH_RANK = 5;
   public static final int STRAIGHT_RANK = 6;
   public static final int THREE_OF_A_KIND_RANK = 7;
   public static final int TWO_PAIR_RANK = 8;
   public static final int PAIR_RANK = 9;
   public static final int HIGH_CARD_RANK = 10;

   static class RankDTO implements Comparable<RankDTO> {
      int rank;
      VisibleCards vc;
      int[] straightTopCardValueAndSuite;
      Map<Integer, List<Integer>> valuesCount;
      Map<Integer, List<Integer>> suitesCount;

      public RankDTO(int rank, VisibleCards vc, int[] straightTopCardValueAndSuite,
            Map<Integer, List<Integer>> valuesCount, Map<Integer, List<Integer>> suitesCount) {
         this.rank = rank;
         this.vc = vc;
         this.straightTopCardValueAndSuite = straightTopCardValueAndSuite;
         this.valuesCount = valuesCount;
         this.suitesCount = suitesCount;
      }

      public int compareTo(RankDTO other) {
         if (this.rank != other.rank) {
            return this.rank - other.rank;
         }

         // same rank
         if (rank == ROYAL_FLUSH_RANK) {
            return 0;
         }

         if (rank == STRAIGHT_FLUSH_RANK || rank == STRAIGHT_RANK) {
            // compare the highest card in the straights
            return Card.compareCardValues(this.straightTopCardValueAndSuite[0],
                  other.straightTopCardValueAndSuite[0]);
         }

         if (rank == FOUR_OF_A_KIND_RANK) {
            int comp = Card.compareCardValues(this.valuesCount.get(4).get(0),
                  other.valuesCount.get(4).get(0));
            if (comp == 0) {
               return Card.compareCardValues(this.valuesCount.get(1).get(0),
                     other.valuesCount.get(1).get(0));
            }
            return comp;
         }

         if (rank == FULL_HOUSE_RANK) {
            int comp = Card.compareCardValues(this.valuesCount.get(3).get(0),
                  other.valuesCount.get(3).get(0));
            if (comp == 0) {
               return Card.compareCardValues(this.valuesCount.get(2).get(0),
                     other.valuesCount.get(2).get(0));
            }
            return comp;
         }

         if (rank == FLUSH_RANK) {
            this.vc.sortCards();
            other.vc.sortCards();

            int len = VisibleCards.SIZE;
            for (int i = 0; i < len; i++) {
               int comp = Card.compareCardValues(this.vc.getCardByIdx(i).value,
                     other.vc.getCardByIdx(i).value);
               if (comp != 0) {
                  return comp;
               }
            }

            return 0;
         }

         if (rank == THREE_OF_A_KIND_RANK) {
            int comp = Card.compareCardValues(this.valuesCount.get(3).get(0),
                  other.valuesCount.get(3).get(0));
            if (comp == 0) {
               int len = VisibleCards.SIZE - 3;
               for (int i = 0; i < len; i++) {
                  int innerComp = Card.compareCardValues(this.valuesCount.get(1).get(i),
                        other.valuesCount.get(1).get(i));
                  if (innerComp != 0) {
                     return innerComp;
                  }
               }
               return 0;
            }
            return comp;
         }

         if (rank == TWO_PAIR_RANK) {
            int len = 2;
            for (int i = 0; i < len; i++) {
               int comp = Card.compareCardValues(this.valuesCount.get(2).get(i),
                     other.valuesCount.get(2).get(i));
               if (comp != 0) {
                  return comp;
               }
            }
            return Card.compareCardValues(this.valuesCount.get(1).get(0),
                  other.valuesCount.get(1).get(0));
         }

         if (rank == PAIR_RANK) {
            int comp = Card.compareCardValues(this.valuesCount.get(2).get(0),
                  other.valuesCount.get(2).get(0));
            if (comp != 0) {
               return comp;
            }

            int len = VisibleCards.SIZE - 2;
            for (int i = 0; i < len; i++) {
               int innerComp = Card.compareCardValues(this.valuesCount.get(1).get(i),
                     other.valuesCount.get(1).get(i));
               if (innerComp != 0) {
                  return innerComp;
               }
            }
            return 0;
         }

         // else
         int len = VisibleCards.SIZE;
         for (int i = 0; i < len; i++) {
            int comp = Card.compareCardValues(this.valuesCount.get(1).get(i),
                  other.valuesCount.get(1).get(i));
            if (comp != 0) {
               return comp;
            }
         }

         return 0;
      }
   }

   private static Map<Integer, List<Integer>> getCountMap(int[][] count, boolean sortByCardValue) {
      Map<Integer, List<Integer>> result = new HashMap<>();

      for (int[] pair : count) {
         int cnt = pair[1], cardValue = pair[0];

         if (!result.containsKey(cnt)) {
            result.put(cnt, new ArrayList<>());
         }
         result.get(cnt).add(cardValue);
      }

      if (sortByCardValue) {
         for (List<Integer> list : result.values()) {
            Collections.sort(list, (a, b) -> Card.compareCardValues(a, b));
         }
      }

      return result;
   }

   private static Map<Integer, List<Integer>> cardValueCount(VisibleCards vc) {
      int[] cnt = new int[Card.CARD_TOTAL_NUMBER_PER_SUITE + 1];
      int len = 0;

      for (int i = 0; i < vc.size(); i++) {
         Card curCard = vc.getCardByIdx(i);

         cnt[curCard.value]++;
         if (cnt[curCard.value] == 1) {
            len++;
         }
      }

      int[][] result = new int[len][]; // [0] = card value, [1] = count
      int totalCnt = 0;
      for (int i = Card.ACE, j = 0; totalCnt < vc.size() && i <= Card.KING; i++) {
         if (cnt[i] > 0) {
            result[j++] = new int[] {i, cnt[i]};
            totalCnt += cnt[i];
         }
      }

      // Arrays.sort(result, (a, b) -> a[1]==b[1] ? Card.compareCardValues(a[0], b[0])
      // : b[1]-a[1]);
      return getCountMap(result, true);
   }

   private static Map<Integer, List<Integer>> cardSuiteCount(VisibleCards vc) {
      int[] cnt = new int[Card.NUMBER_OF_SUITE];
      int len = 0;

      for (int i = 0; i < vc.size(); i++) {
         Card curCard = vc.getCardByIdx(i);

         cnt[curCard.suite]++;
         if (cnt[curCard.suite] == 1) {
            len++;
         }
      }

      int[][] result = new int[len][]; // [0] = card suite, [1] = count
      for (int i = Card.LOW_SUITE, j = 0; i <= Card.HIGH_SUITE; i++) {
         if (cnt[i] > 0) {
            result[j++] = new int[] {i, cnt[i]};
         }
      }

      // Arrays.sort(result, (a, b) -> b[1]-a[1]);
      return getCountMap(result, false);
   }

   private static int[] straight(VisibleCards vc) {
      // {top card, suite}
      if (vc.size() == 0) {
         return null;
      }

      Set<Integer> set = new HashSet<>();
      int suite = vc.getCardByIdx(0).suite;
      int maxValue = -1;
      int secondMaxValue = -1;

      for (int i = 0; i < vc.size(); i++) {
         Card curCard = vc.getCardByIdx(i);
         if (suite != curCard.suite) {
            suite = -1;
         }
         set.add(curCard.value);

         if (maxValue != Card.ACE && (curCard.value == Card.ACE || curCard.value > maxValue)) {
            secondMaxValue = maxValue;
            maxValue = curCard.value;
         } else if (curCard.value != Card.ACE && curCard.value > secondMaxValue) {
            secondMaxValue = curCard.value;
         }
      }

      for (int nextStart : new int[] {maxValue, secondMaxValue}) {
         int next = Card.getNextLargerValue(nextStart, false);
         int len = 1;
         while (set.contains(next)) {
            len++;
            next = Card.getNextLargerValue(next, true);
         }

         if (len >= VisibleCards.SIZE) {
            return new int[] {nextStart, suite};
         }
      }

      return null;
   }

   public static RankDTO computeRank(VisibleCards vc) {
      int rank = HIGH_CARD_RANK;
      int[] topStraight = straight(vc);
      Map<Integer, List<Integer>> valuesCount = cardValueCount(vc);
      Map<Integer, List<Integer>> suitesCount = cardSuiteCount(vc);

      if (isRoyalFlush(topStraight)) {
         rank = ROYAL_FLUSH_RANK;
      } else if (isStraightFlush(topStraight)) {
         rank = STRAIGHT_FLUSH_RANK;
      } else if (isFourOfAKind(valuesCount)) {
         rank = FOUR_OF_A_KIND_RANK;
      } else if (isFullHouse(valuesCount)) {
         rank = FULL_HOUSE_RANK;
      } else if (isFlush(suitesCount)) {
         rank = FLUSH_RANK;
      } else if (isStraight(topStraight)) {
         rank = STRAIGHT_RANK;
      } else if (isThreeOfAKind(valuesCount)) {
         rank = THREE_OF_A_KIND_RANK;
      } else if (isTwoPair(valuesCount)) {
         rank = TWO_PAIR_RANK;
      } else if (isPair(valuesCount)) {
         rank = PAIR_RANK;
      }

      return new RankDTO(rank, vc.clone(), topStraight, valuesCount, suitesCount);
   }

   public static int compareCards(VisibleCards vc1, VisibleCards vc2) {
      return computeRank(vc1).compareTo(computeRank(vc2));
   }

   private static boolean isRoyalFlush(int[] res) {
      return res != null && res[0] == Card.ACE && res[1] != -1;
   }

   private static boolean isStraightFlush(int[] res) {
      return res != null && res[1] != -1;
   }

   private static boolean isFourOfAKind(Map<Integer, List<Integer>> valueCount) {
      return valueCount.containsKey(4);
   }

   private static boolean isFullHouse(Map<Integer, List<Integer>> valueCount) {
      return valueCount.containsKey(3) && valueCount.containsKey(2);
   }

   private static boolean isFlush(Map<Integer, List<Integer>> suitesCount) {
      return suitesCount.containsKey(5);
   }

   private static boolean isStraight(int[] res) {
      return res != null && res[1] == -1;
   }

   private static boolean isThreeOfAKind(Map<Integer, List<Integer>> valueCount) {
      return valueCount.containsKey(3) && !valueCount.containsKey(2);
   }

   private static boolean isTwoPair(Map<Integer, List<Integer>> valueCount) {
      return valueCount.containsKey(2) && valueCount.get(2).size() >= 2;
   }

   private static boolean isPair(Map<Integer, List<Integer>> valueCount) {
      return valueCount.containsKey(2) && valueCount.get(2).size() == 1;
   }
}
