package edu.gatech.epidemics.api;

import edu.gatech.epidemics.entities.User;
import edu.gatech.epidemics.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author atalati
 * <p>
 * /api/user
 */
@RestController
public class UserApiController {

    @Autowired
    Environment environment;

    @Autowired
    private UserService userService;

    @RequestMapping("/api/user/hello")
    public String Hello() throws UnknownHostException {
        String datePattern = "MM/dd/yyyy HH:mm:ss";
        DateTimeFormatter df = DateTimeFormatter.ofPattern(datePattern);
        LocalDateTime now = LocalDateTime.now();

        String hostName = InetAddress.getLocalHost().getHostName();
        String port = environment.getProperty("local.server.port");

        return "Do you speak whale? - Web server @ " + df.format(now) + ". <br/><br/><br/><b>Host:</b> " + hostName
                + " <span style=\"color:red;\">:</span> " + port;
    }

    /*
        GET: /api/user/
        Returns all users from the database
     */
    @GetMapping(value = "/api/user")
    public List<User> get() {
        return userService.findAll();
    }

    @GetMapping(value = "/api/user/{id}")
    public User get(@PathVariable int id) {
        return userService.findById(id);
    }
}