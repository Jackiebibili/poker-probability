# Poker Odds Calculator
Computes the pre-flop odds for each players, given their hands. The result includes winning chance and tying chance.

## How to Use It
Check out `src/App.java` for an example
```java
// 1. initialize a list of players' hands
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

// 2. create an instance of `Poker` class
Poker poker = new Poker();

// 3. invoke the `calcPlayerChance` to calculate the result concurrently
OddsWrapper res = poker.calcPlayerChance(playersHands);
```

## Sample Ouput in Text
Results in `OddsWrapper` can be easily extracted and displayed for information.
```text
3_0//3_2//winning chance 15.286% # means hands of three of clubs and three of hearts
        tying chance 0.245%

2_0//2_2//winning chance 13.893%
        tying chance 0.245%

7_0//6_0//winning chance 4.666%
        tying chance 0.792%

5_0//4_0//winning chance 0.000%
        tying chance 3.534%

8_0//9_0//winning chance 11.582%
        tying chance 0.245%

5_1//4_1//winning chance 6.918%
        tying chance 3.534%

11_2//10_2//winning chance 20.796%
        tying chance 0.245%

5_2//4_2//winning chance 0.188%
        tying chance 3.534%

5_3//6_1//winning chance 1.504%
        tying chance 1.050%

13_1//12_3//winning chance 21.086% # means hands of king of diamounds and queen of spades
        tying chance 0.245%
```