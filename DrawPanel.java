import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.awt.Font;

class DrawPanel extends JPanel implements MouseListener {

    private ArrayList<Card> hand;
    private ArrayList<Card> deck;
    private Rectangle replaceButton;
    private Rectangle newGameButton;
    private final int numRows = 3;
    private final int numCols = 3;
    private final int cardSpacing = 10;
    private final Set<Card> highlightedCards = new HashSet<>();
    private boolean gameWon = false;
    private boolean gameLost = false;

    public DrawPanel() {
        replaceButton = new Rectangle(147, 400, 160, 26);
        newGameButton = new Rectangle(147, 450, 160, 26);
        this.addMouseListener(this);
        deck = Card.buildDeck();
        hand = Card.buildHand(deck, 9);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!hand.isEmpty()) {
            int x = 50;
            int y = 10;
            int cardWidth = hand.get(0).getImage().getWidth();
            int cardHeight = hand.get(0).getImage().getHeight();

            for (int i = 0; i < hand.size(); i++) {
                Card c = hand.get(i);
                if (c.getHighlight()) {
                    g.drawRect(x - 2, y - 2, cardWidth + 4, cardHeight + 4);
                }
                c.setRectangleLocation(x, y);
                g.drawImage(c.getImage(), x, y, null);
                x = x + cardWidth + cardSpacing;
                if ((i + 1) % numCols == 0) {
                    x = 50;
                    y = y + cardHeight + cardSpacing;
                }
            }
        }
        g.setFont(new Font("Courier New", Font.BOLD, 20));
        g.drawString("Replace cards", 150, 420);
        g.drawRect((int) replaceButton.getX(), (int) replaceButton.getY(), (int) replaceButton.getWidth(), (int) replaceButton.getHeight());
        g.drawString("New game", 150, 470);
        g.drawRect((int) newGameButton.getX(), (int) newGameButton.getY(), (int) newGameButton.getWidth(), (int) newGameButton.getHeight());
        g.drawString("Cards left in deck: " + deck.size(), 150, 520);
        if (gameWon) {
            g.drawString("You win! No cards left in the deck.", 100, 550);
        }
        if (gameLost) {
            g.drawString("You lose! No valid moves left.", 100, 550);
        }
    }

    public void mousePressed(MouseEvent e) {

        Point clicked = e.getPoint();

        if (replaceButton.contains(clicked)) {
            replaceHighlightedCards();
        }

        if (newGameButton.contains(clicked)) {
            startNewGame();
        }

        for (int i = 0; i < hand.size(); i++) {
            Rectangle box = hand.get(i).getCardBox();
            if (box.contains(clicked)) {
                hand.get(i).flipHighlight();
                if (hand.get(i).getHighlight()) {
                    highlightedCards.add(hand.get(i));
                } else {
                    highlightedCards.remove(hand.get(i));
                }
            }
        }

        if (deck.isEmpty() && hand.isEmpty()) {
            gameWon = true;
            gameLost = false;
        } else if (!hasValidMove()) {
            gameLost = true;
            gameWon = false;
        }
    }

    private void replaceHighlightedCards() {
        if (highlightedCards.size() == 2) {
            Card[] cards = highlightedCards.toArray(new Card[2]);
            if (Card.canBeRemoved(cards[0], cards[1])) {
                hand.removeAll(highlightedCards);
                highlightedCards.clear();
                while (hand.size() < 9 && !deck.isEmpty()) {
                    hand.add(Card.replaceCard(deck));
                }
            }
        } else if (highlightedCards.size() == 3) {
            Card[] cards = highlightedCards.toArray(new Card[3]);
            if (Card.canBeRemoved(cards[0], cards[1], cards[2])) {
                hand.removeAll(highlightedCards);
                highlightedCards.clear();
                while (hand.size() < 9 && !deck.isEmpty()) {
                    hand.add(Card.replaceCard(deck));
                }
            }
        }
    }

    private void startNewGame() {
        deck = Card.buildDeck();
        hand = Card.buildHand(deck, 9);
        highlightedCards.clear();
        gameWon = false;
        gameLost = false;
    }

    private boolean hasValidMove() {
        for (int i = 0; i < hand.size(); i++) {
            for (int j = i + 1; j < hand.size(); j++) {
                if (Card.canBeRemoved(hand.get(i), hand.get(j))) {
                    return true;
                }
                for (int k = j + 1; k < hand.size(); k++) {
                    if (Card.canBeRemoved(hand.get(i), hand.get(j), hand.get(k))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }
}
