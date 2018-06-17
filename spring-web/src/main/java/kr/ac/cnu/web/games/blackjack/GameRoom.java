package kr.ac.cnu.web.games.blackjack;

import kr.ac.cnu.web.model.User;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rokim on 2018. 5. 26..
 */
public class GameRoom {
    @Getter
    private final String roomId;
    @Getter
    private final Dealer dealer;
    @Getter
    private final Map<String, Player> playerList;
    @Getter
    private final Deck deck;
    @Getter
    private boolean isFinished;
    private final Evaluator evaluator;

    public GameRoom(Deck deck) {
        this.roomId = UUID.randomUUID().toString();
        this.deck = deck;
        this.dealer = new Dealer(new Hand(deck));
        this.playerList = new HashMap<>();
        this.evaluator = new Evaluator(playerList, dealer);
        this.isFinished = true;
    }

    public void addPlayer(String playerName, long seedMoney) {
        Player player = new Player(seedMoney, new Hand(deck));

        playerList.put(playerName, player);
    }

    public void removePlayer(String playerName) {
        playerList.remove(playerName);
    }

    public void reset() {
        dealer.reset();
        playerList.forEach((s, player) -> player.reset());
    }

    public void bet(String name, long bet) {
        Player player = playerList.get(name);

        player.placeBet(bet);
    }
    public long update(String name){
        Player player = playerList.get(name);
        return player.getBalance();
    }

    public void deal() {
        this.isFinished = false;
        dealer.deal();
        playerList.forEach((s, player) -> player.deal());
    }

    public void hit(String name) {
        Player player = playerList.get(name);


        // toDo doubledown check
        if (player.isDD()) {
            player.hitCard();
            player.stand();
            playDealer();
        } else {
            player.hitCard();
        }

        if(playerList.get(name).getHand().getCardSum() >21){
            playDealer();
        }

    }

    public void stand(String name) {
        Player player = playerList.get(name);
        player.stand();
    }

    public void playDealer() {
        dealer.play();
        evaluator.evaluate();
        this.isFinished = true;
    }

    public void doubledown(String name) {
        Player player = playerList.get(name);
        player.doubledownCard();
    }
}
