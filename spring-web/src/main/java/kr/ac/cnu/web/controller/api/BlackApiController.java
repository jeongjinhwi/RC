package kr.ac.cnu.web.controller.api;

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

    @RequestMapping(value = "/jpa", method = RequestMethod.GET)
    public Page<User> jpa(@RequestParam int size) {
        PageRequest pageRequest = PageRequest.of(0, size);

        Page<User> page = userRepository.findAll(pageRequest);

        return page;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User login(@RequestBody String name) {
        return userRepository.findById(name).orElseThrow(() -> new NoUserException());
    }

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User singup(@RequestBody String name) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO user (name, account) VALUES (?, ?)");
        pstmt.setString(1, name);
        pstmt.setInt(2, 50000);
        Optional<User> userOptional = userRepository.findById(name);
        if (userOptional.isPresent()) {
            throw new RuntimeException();
        }

        pstmt.execute();
        User user = new User(name, 50000);

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