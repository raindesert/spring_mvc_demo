package com.demo;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Controller {

	@RequestMapping(value = "/h1",method = RequestMethod.POST)
    public String sayHi(@RequestBody DemoBo bo){
        return "Hi "+bo.getName()+", values "+bo.getValue();
    }
}
