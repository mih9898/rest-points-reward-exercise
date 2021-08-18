package org.points_rewards.controller;

import org.points_rewards.entity.Payer;
import org.points_rewards.repository.GenericDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class MyRestController {
    private final GenericDao genericDao;

    @Autowired
    public MyRestController(GenericDao genericDao) {
        this.genericDao = genericDao;
    }


    @GetMapping("/payers")
    public List<Payer> getPayers() {
        return genericDao.getAll(Payer.class);
    }
}
