package kr.ac.cnu.web.games.blackjack;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rokim on 2018. 5. 26..
 */
public class Hand {
    private Deck deck;
    @Getter
    private List<Card> cardList = new ArrayList<>();

    public Hand(Deck deck) {
        this.deck = deck;
    }

    public Card drawCard() {
        Card card = deck.drawCard();
        cardList.add(card);
        return card;
    }

    /*public int getCardSum() {
        return cardList.stream().mapToInt(card -> card.getRank()).sum();
    }
    */

    public int getCardSum(){
        int sum = 0;
        for (int i = 0 ; i < cardList.size() ; i ++) {
            if (cardList.get(i).getRank() < 11) {
                if(cardList.get(i).getRank()==1){
                    sum += 11;
                }
                else{
                    sum = sum + cardList.get(i).getRank();
                }
            }
            else {
                sum = sum + 10;
            }
            for (int j = 0 ; j < cardList.size() ; j ++) {
                if(cardList.get(j).getRank()==1 && sum > 21){
                    sum -= 10;
                }
            }
        }

        return sum;
    }

    public void reset() {
        cardList.clear();
    }
}
