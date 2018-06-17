package kr.ac.cnu.web.games.blackjack;

import java.util.Map;

/**
 * Created by rokim on 2018. 5. 27..
 */
public class Evaluator {
    private Map<String, Player> playerMap;
    private Dealer dealer;

    public Evaluator(Map<String, Player> playerMap, Dealer dealer) {
        this.playerMap = playerMap;
        this.dealer = dealer;
    }

    public boolean evaluate() {
        if (playerMap.values().stream().anyMatch(player -> player.isPlaying())) {
            return false;
        }

        int dealerResult = dealer.getHand().getCardSum();

        if (dealerResult > 21) {
            playerMap.forEach((s, player) -> player.win());

            return true;
        }

        playerMap.forEach((s, player) -> {
            int playerResult = player.getHand().getCardSum();

            if(player.getHand().getCardList().size() == 2) { // 만약 플레이어가 2장의 카드만 가지고 있다면 (블랙잭 조건)
                if (playerResult == 21) { // 또한, 플레이어가 블랙잭을 만족한다면
                    if (dealerResult == 21)
                        player.tie();
                    else {
                        player.blackjackwin(); // 1.5배의 배당금을 받는 함수 실행
                    }
                } else {
                }
                if (playerResult > 21) {
                    player.lost();
                } else if (playerResult > dealerResult) {
                    player.win();
                } else if (playerResult == dealerResult) {
                    player.tie();
                } else {
                    player.lost();
                }
            }
        });

        return true;
    }


}
