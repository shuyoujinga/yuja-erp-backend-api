package org.jeecg.modules.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/suppliers/test")
public class TestController {
    @GetMapping(value = "/list")
    public String list() {
        return "system/list";
    }
}
