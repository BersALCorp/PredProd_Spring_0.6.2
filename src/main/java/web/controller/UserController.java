package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.Car;
import web.model.User;
import web.service.UserTableService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserTableService userTableService;

    @Autowired
    public UserController(UserTableService userTableService) {
        this.userTableService = userTableService;
    }

    @GetMapping("users")
    public String showUserTable(Model model) {
        List<User> userList = userTableService.listSortById(User.class);
        model.addAttribute("userList", userList);
        return "usertable";
    }

    @PostMapping("saveUser")
    public ResponseEntity<Map<String, Object>> saveUser(@RequestBody User user) {
        try {
            System.out.println(user);
            userTableService.add(user);
            User savedUser = userTableService.getById(User.class, user.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User saved successfully.");
            response.put("user", savedUser);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("saveCar")
    public ResponseEntity<Map<String, String>> saveCar(@RequestBody Map<String, Object> requestData) {
        try {
            long userId = Long.parseLong(requestData.get("userId").toString());
            Map<String, Object> carData = (Map<String, Object>) requestData.get("carData");
            Car car = new Car(
                    carData.get("brand").toString(),
                    carData.get("series").toString(),
                    carData.get("model").toString(),
                    carData.get("color").toString()
            );
            userTableService.saveCarForUser(car, userId);
            return ResponseEntity.ok(Map.of("message", "Car saved successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("updateCar")
    public ResponseEntity<Map<String, String>> updateCar(@RequestBody Map<String, Object> requestData) {
        try {
            long userId = Long.parseLong(requestData.get("userId").toString());
            Map<String, Object> carData = (Map<String, Object>) requestData.get("carData");
            Car car = new Car(
                    carData.get("brand").toString(),
                    carData.get("series").toString(),
                    carData.get("model").toString(),
                    carData.get("color").toString()
            );

            userTableService.updateCar(car, userId);
            return ResponseEntity.ok(Map.of("message", "Car update successfully."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("getcar")
    public ResponseEntity<Map<String, Object>> getCar(@RequestParam long id) {
        try {
            Car tempCar = userTableService.getById(User.class, id).getCar();
            if (tempCar == null) tempCar = new Car();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Car gets successfully.");
            response.put("car", tempCar);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("updateUser")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        try {
            userTableService.updateUser(user);
            return ResponseEntity.ok("User id: " + user.getId() + " update successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error update user.");
        }
    }

    @PostMapping("deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam long id) {
        try {
            userTableService.deleteById(User.class, id);
            return ResponseEntity.ok("User id: " + id + " deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }
    }

    @PostMapping("deleteCar")
    public ResponseEntity<String> deleteCar(@RequestParam long id) {
        try {
            userTableService.deleteCarFromUser(id);
            return ResponseEntity.ok("Car from id: " + id + " deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting car");
        }
    }

    @PostMapping("resetTable")
    public ResponseEntity<String> resetTable() {
        try {
            userTableService.resetTable();
            return ResponseEntity.ok(" deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }
    }

    @PostMapping("recreateTable")
    public ResponseEntity<String> recreateTable() {
        try {
            userTableService.recreateTable();
            return ResponseEntity.ok(" deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }
    }
}