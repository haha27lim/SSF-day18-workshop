package com.example.workshop18.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.workshop18.model.Love;
import com.example.workshop18.service.LoveCalculator;

@Controller
@RequestMapping(path="/lovecalculate")
public class LoveController {
    
    @Autowired
    private LoveCalculator loveCal;

    @GetMapping
    public String getCalculate (@RequestParam(required=true) String firstName,
        @RequestParam(required=true) String secondName, Model model) throws IOException {
        Optional<Love> l= loveCal.getCalculate(firstName, secondName);
        model.addAttribute("calculate", l.get());
        return "calculate";    
    }

    @GetMapping(path = "/list")
    public String getAllLoveCompat(Model model) throws IOException {
        Love[] mArr = loveCal.getAllMatchMaking();
        model.addAttribute("arr", mArr);
        return "list";
    }
    
}

