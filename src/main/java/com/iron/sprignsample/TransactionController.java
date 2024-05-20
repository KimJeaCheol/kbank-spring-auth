package com.iron.sprignsample;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisService redisService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateTransactionId(@RequestHeader("CI") String ci, HttpServletRequest request, @RequestBody TransactionInfo transactionInfo) {
        String token = jwtTokenProvider.generateToken(ci, transactionInfo);
        // CI를 HttpSession에 저장
        HttpSession session = request.getSession();
        session.setAttribute("CI", ci);

        redisService.saveToken(ci, token, Duration.ofMinutes(3));
        return ResponseEntity.ok(token);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyTransactionId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String ci = (String) session.getAttribute("CI");

        if (ci == null) {
            return ResponseEntity.badRequest().body("CI not found in session");
        }

        String storedToken = redisService.getToken(ci);

        if (storedToken == null) {
            return ResponseEntity.badRequest().body("Token not found in Redis");
        }

        if (!jwtTokenProvider.validateToken(storedToken)) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        Claims claims = jwtTokenProvider.getAllClaimsFromToken(storedToken);
        return ResponseEntity.ok("Token is valid. Claims: " + claims.toString());
        // return ResponseEntity.ok("Token is valid");
    }
}