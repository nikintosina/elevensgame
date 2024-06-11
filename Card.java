import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Rectangle;

public class Card {
    private String suit;
    private String value;
    private String imageFileName;
    private BufferedImage image;
    private Rectangle cardBox;
    private boolean highlight;

    public Card(String suit, String value) {
        this.suit = suit;
        this.value = value;
        this.imageFileName = "images/card_" + suit + "_" + value + ".png";
        this.image = readImage();
        this.cardBox = new Rectangle(-100, -100, image.getWidth(), image.getHeight());
        this.highlight = false;
    }

    public Rectangle getCardBox() {
        return cardBox;
    }

    public String getSuit() {
        return suit;
    }

    public void setRectangleLocation(int x, int y) {
        cardBox.setLocation(x, y);
    }

    public String getValue() {
        return value;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public String toString() {
        return suit + " " + value;
    }

    public void flipHighlight() {
        highlight = !highlight;
    }

    public boolean getHighlight() {
        return highlight;
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage readImage() {
        try {
            return ImageIO.read(new File(imageFileName));
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public static ArrayList<Card> buildDeck() {
        ArrayList<Card> deck = new ArrayList<Card>();
        String[] suits = {"clubs", "diamonds", "hearts", "spades"};
        String[] values = {"02", "03", "04", "05", "06", "07", "08", "09", "10", "A", "J", "K", "Q"};
        for (String s : suits) {
            for (String v : values) {
                Card c = new Card(s, v);
                deck.add(c);
            }
        }
        return deck;
    }

    public static ArrayList<Card> buildHand(ArrayList<Card> deck, int numCards) {
        ArrayList<Card> hand = new ArrayList<Card>();
        for (int i = 0; i < numCards; i++) {
            int r = (int) (Math.random() * deck.size());
            Card c = deck.remove(r);
            hand.add(c);
        }
        return hand;
    }

    public static Card replaceCard(ArrayList<Card> deck) {
        int r = (int) (Math.random() * deck.size());
        return deck.remove(r);
    }

    public static boolean canBeRemoved(Card card1, Card card2) {
        int value1 = card1.getNumericValue();
        int value2 = card2.getNumericValue();
        return value1 + value2 == 11;
    }

    public static boolean canBeRemoved(Card card1, Card card2, Card card3) {
        return (card1.isFaceCard() && card2.isFaceCard() && card3.isFaceCard() &&
                !card1.getValue().equals(card2.getValue()) &&
                !card1.getValue().equals(card3.getValue()) &&
                !card2.getValue().equals(card3.getValue()));
    }

    public int getNumericValue() {
        switch (value) {
            case "A":
                return 1;
            case "02":
                return 2;
            case "03":
                return 3;
            case "04":
                return 4;
            case "05":
                return 5;
            case "06":
                return 6;
            case "07":
                return 7;
            case "08":
                return 8;
            case "09":
                return 9;
            case "10":
                return 10;
            default:
                return 0;
        }
    }

    public boolean isFaceCard() {
        return value.equals("J") || value.equals("Q") || value.equals("K");
    }
}
