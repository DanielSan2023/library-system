package com.springboot.librarysystem.controller;

import com.springboot.librarysystem.security.MonetaryAmount;
import lombok.AllArgsConstructor;
import org.slf4j.*;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class AccountController {
    public static Logger logger = LoggerFactory.getLogger(BookController.class);


    @GetMapping("/accounts")
    @Secured({"ROLE_USER","ROLE_ADMIN"})
    public BigDecimal getAccount(@AuthenticationPrincipal User user) {

        logger.info("Retrieving status for active account: {}  with roles {}  ",
                user.getUsername(), user.getAuthorities());
        return BigDecimal.ONE;
    }

    @PostMapping("/accounts/withdrawals")
    @Secured("ROLE_ADMIN")
    public void withdraw(@RequestBody MonetaryAmount amount) {

        logger.info("Withdrawing {} ", amount);
    }

}
