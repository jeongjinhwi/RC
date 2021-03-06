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
    private boolean isDD;

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
            throw new NotEnoughBalanceException();
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
    }

    public void win() {
        balance += currentBet * 2;
        currentBet = 0;
    }

    public void tie() {
        balance += currentBet;
        currentBet = 0;
    }

    public void lost() {
        currentBet = 0;
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
        this.currentBet = this.getCurrentBet() *2;
    }
}
