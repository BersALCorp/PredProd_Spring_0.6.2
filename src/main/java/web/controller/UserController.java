package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.Car;
import web.model.User;
import web.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users")
    public String showUserTable(Model model) {
        List<User> userList = userService.listSortById(User.class);
        model.addAttribute("userList", userList);
        return "usertable";
    }

    @PostMapping("saveUser")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("saveCar")
    public ResponseEntity<String> saveCar(@RequestParam("userId") long userId, @RequestBody Car car) {
        try {
            userService.saveCarForUser(car, userId);
            return ResponseEntity.ok("Car saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error save car.");
        }
    }

    @PutMapping("updateCar")
    public ResponseEntity<String> updateCar(@RequestParam("userId") long userId, @RequestBody Car car) {
        try {
            userService.updateCar(car, userId);
            return ResponseEntity.ok("Car update successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error update car.");
        }
    }

    @GetMapping("getcar")
    public ResponseEntity<Car> getCar(@RequestParam long id) {
        try {
            Car car = userService.getById(User.class, id).getCar();
            if (car == null) car = new Car();
            return ResponseEntity.ok(car);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("updateUser")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        try {
            userService.updateUser(user);
            return ResponseEntity.ok("User id: " + user.getId() + " update successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error update user.");
        }
    }

    @DeleteMapping("deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam long id) {
        try {
            userService.deleteById(User.class, id);
            return ResponseEntity.ok("User id: " + id + " deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }
    }

    @DeleteMapping("deleteCar")
    public ResponseEntity<String> deleteCar(@RequestParam long id) {
        try {
            userService.deleteCarFromUser(id);
            return ResponseEntity.ok("Car from id: " + id + " deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting car");
        }
    }

    @DeleteMapping("resetTable")
    public ResponseEntity<String> resetTable() {
        try {
            userService.resetTable();
            return ResponseEntity.ok("Reset table successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reset table");
        }
    }

    @DeleteMapping("recreateTable")
    public ResponseEntity<String> recreateTable() {
        try {
            userService.recreateTable();
            return ResponseEntity.ok("Recreate table successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error recreate table");
        }
    }
}