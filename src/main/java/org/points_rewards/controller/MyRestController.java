package org.points_rewards.controller;

import org.points_rewards.entity.Payer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class MyRestController {

    @GetMapping("/payers")
    public List<Payer> getPayers() {
        List<Payer> payers = new ArrayList<>();
        Payer payer = new Payer("name1", 100);
        Payer payer2 = new Payer("name2", 200);
        payers.add(payer);
        payers.add(payer2);
        return payers;
    }
}
