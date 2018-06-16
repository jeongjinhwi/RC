package kr.ac.cnu.web.games.blackjack;

import kr.ac.cnu.web.exceptions.NotEnoughBalanceException;
import kr.ac.cnu.web.exceptions.TooMuchBetException;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by rokim on 2018. 5. 26..
 */
public class Player {
    @Getter
    private long balance;
    @Getter
    private long currentBet;
    @Getter
    private boolean isPlaying;
    @Getter
    private Hand hand;
    @Getter
//<<<<<<< Updated upstream
    private boolean isDD;

    //=======
//>>>>>>> Stashed changes
    public Player(long seedMoney, Hand hand) {
        this.balance = seedMoney;
        this.hand = hand;

        isPlaying = false;
        isDD = false;
    }

    public void reset() {
        hand.reset();
        isPlaying = false;
        isDD = false;
    }

    public void placeBet(long bet) {
        if(balance < bet) {
            bet = balance;
        }
        if(bet > 10000){
            throw new TooMuchBetException();
        }
        balance -= bet;
        currentBet = bet;

        isPlaying = true;
    }

    public void deal() {
        hand.drawCard();
        hand.drawCard();
        if(hand.getCardSum()>21){
            lost();
        }
    }

    public void win() {
        balance += currentBet;
    }

    public void tie() {

    }

    public void lost() {
        balance -= currentBet;
    }

    public Card hitCard() {
        return hand.drawCard();
    }

    public void stand() {
        this.isPlaying = false;
    }

    public void doubledownCard() {
        this.isDD = true;
        this.balance -= this.getCurrentBet();
        this.currentBet = this.getCurrentBet()*2;
    }
}