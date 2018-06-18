package kr.ac.cnu.web.controller.api;

import kr.ac.cnu.web.games.blackjack.Player;
import lombok.Getter;
import kr.ac.cnu.web.exceptions.NoLoginException;
import kr.ac.cnu.web.exceptions.NoUserException;
import kr.ac.cnu.web.games.blackjack.GameRoom;
import kr.ac.cnu.web.model.User;
import kr.ac.cnu.web.repository.UserRepository;
import kr.ac.cnu.web.service.BlackjackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Created by rokim on 2018. 5. 21..
 */
@RestController
@RequestMapping("/api/black-jack")
@CrossOrigin
public class BlackApiController {
    @Autowired
    private BlackjackService blackjackService;
    @Autowired
    private UserRepository userRepository;
    @Getter
    private String username;

    @PostMapping(value = "/rank")
    public List<User> rank() {
        List<User> rankList = userRepository.findAll();
        rankList.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if(o1.getAccount() < o2.getAccount()){
                    return 1;
                }
                else if( o1.getAccount() == o2.getAccount()){
                    return 0;
                }
                else{
                    return -1;
                }
            }
        });
        return rankList;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User login(@RequestBody String name) {
        username = name;
        return userRepository.findById(name).orElseThrow(() -> new NoUserException());
    }

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User singup(@RequestBody String name) {
        // TODO check already used name
        Optional<User> userOptional = userRepository.findById(name);
        if (userOptional.isPresent()) {
            throw new RuntimeException();
        }

        // TODO new user
        User user = new User(name, 50000);

        // TODO save in repository
        return userRepository.save(user);
    }
    @PostMapping(value = "/rooms/{roomId}/update")
    public User update(@RequestHeader("name") String name, @PathVariable String roomId) {
        User user = this.getUserFromSession(name);
        long balance = blackjackService.getPlayerBalance(roomId, user);
        user.setAccount(balance);
        return userRepository.save(user);
    }





    @PostMapping("/rooms")
    public GameRoom createRoom(@RequestHeader("name") String name) {
        User user = this.getUserFromSession(name);

        return blackjackService.createGameRoom(user);
    }

    @PostMapping(value = "/rooms/{roomId}/bet", consumes = MediaType.APPLICATION_JSON_VALUE)
    public GameRoom bet(@RequestHeader("name") String name, @PathVariable String roomId, @RequestBody long betMoney) {
        User user = this.getUserFromSession(name);

        return blackjackService.bet(roomId, user, betMoney);
    }

    @PostMapping("/rooms/{roomId}/hit")
    public GameRoom hit(@RequestHeader("name") String name, @PathVariable String roomId) {
        User user = this.getUserFromSession(name);

        return blackjackService.hit(roomId, user);
    }

    @PostMapping("/rooms/{roomId}/stand")
    public GameRoom stand(@RequestHeader("name") String name, @PathVariable String roomId) {
        User user = this.getUserFromSession(name);

        return blackjackService.stand(roomId, user);
    }

    @PostMapping("/rooms/{roomId}/doubledown")
    public GameRoom doubledown(@RequestHeader("name") String name, @PathVariable String roomId) {
        User user = this.getUserFromSession(name);

        return blackjackService.doubledown(roomId, user);
    }

    @GetMapping("/rooms/{roomId}")
    public GameRoom getGameRoomData(@PathVariable String roomId) {
        return blackjackService.getGameRoom(roomId);
    }


    private User getUserFromSession(String name) {
        return userRepository.findById(name).orElseThrow(() -> new NoLoginException());
    }
}