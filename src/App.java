import java.util.*;

public class App {
        public static void main(String[] args) throws Exception {
                List<Hands> playersHands = new ArrayList<>();
                playersHands.add(new Hands(new Card(3, Card.CLUB), new Card(3, Card.HEART)));
                playersHands.add(new Hands(new Card(2, Card.CLUB), new Card(2, Card.HEART)));
                playersHands.add(new Hands(new Card(7, Card.CLUB), new Card(6, Card.CLUB)));
                playersHands.add(new Hands(new Card(5, Card.CLUB), new Card(4, Card.CLUB)));
                playersHands.add(new Hands(new Card(8, Card.CLUB), new Card(9, Card.CLUB)));
                playersHands.add(new Hands(new Card(5, Card.DIAMOUND), new Card(4, Card.DIAMOUND)));
                playersHands.add(new Hands(new Card(11, Card.HEART), new Card(10, Card.HEART)));
                playersHands.add(new Hands(new Card(5, Card.HEART), new Card(4, Card.HEART)));
                playersHands.add(new Hands(new Card(5, Card.SPADE), new Card(6, Card.DIAMOUND)));
                playersHands.add(new Hands(new Card(13, Card.DIAMOUND), new Card(12, Card.SPADE)));

                Date start = new Date();

                Poker poker = new Poker();
                OddsWrapper res = poker.calcPlayerChance(playersHands);

                Date end = new Date();
                System.out.println(((end.getTime() - start.getTime()) / 1000.0) + "s");

                int len = playersHands.size();
                int totalComb = res.total;

                for (int i = 0; i < len; i++) {
                        Hands h = playersHands.get(i);
                        double winningChance = 100 * ((double) res.wins[i]) / totalComb;
                        double tyingChance = 100 * ((double) res.ties[i]) / totalComb;

                        System.out.print(h.toString() + "winning chance ");
                        System.out.printf("%.3f%%", winningChance);

                        System.out.print("\n\ttying chance ");
                        System.out.printf("%.3f%%%n%n", tyingChance);
                }

                // poker.testCnt.toString();
                // int sum = 0;
                // System.out.println("________________");
                // for(int val : poker.testCnt.values()) {
                // System.out.printf("%.3f%n", val * 100 / 1712304.0);
                // }
                // System.out.print(" " + sum);

                // System.out.println("");

                // VisibleCards vc = new VisibleCards(
                // new Card(11,0),
                // new Card(12,0),
                // new Card(13,0),
                // new Card(1,0),
                // new Card(2,0)
                // );

                // Hands myh = new Hands(
                // new Card(10,0),
                // new Card(9,0)
                // );

                // VisibleCards best = vc.getBestCombination(myh);

                // System.out.println(best.toString());
                // if(out1 != null)
                // System.out.println(out1[0] + " " + out1[1]);

                // public void setPlayersHands() {
                // playersHands = new ArrayList<>();
                // playersHands.add(new Hands(
                // new Card(12, 0),
                // new Card(12, 1)));
                // playersHands.add(new Hands(
                // new Card(13, 0),
                // new Card(13, 1)));
                // }
        }

}
