package hei.school.restaurant.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthRestController {

    @GetMapping("/")
    public String hello(){
        return "Hello World";
    }
}
