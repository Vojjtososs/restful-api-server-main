package utb.fai.RESTAPIServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<MyUser>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<MyUser>> searchUsers(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String email,
            @RequestParam(required = false, defaultValue = "") String phoneNumber) {
        return ResponseEntity.ok(userRepository.searchUsers(name, email, phoneNumber));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MyUser> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<MyUser> createUser(@RequestBody MyUser user) {
        if (!user.isUserDataValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            MyUser savedUser = userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MyUser> updateUser(@PathVariable Long id, @RequestBody MyUser userDetails) {
        if (!userDetails.isUserDataValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(userDetails.getName());
            existingUser.setEmail(userDetails.getEmail());
            existingUser.setPhoneNumber(userDetails.getPhoneNumber());
            try {
                MyUser updatedUser = userRepository.save(existingUser);
                return ResponseEntity.ok(updatedUser);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((MyUser) null);
            }
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MyUser> deleteUser(@PathVariable Long id) {
        Optional<MyUser> userOptional = userRepository.findById(id);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); 
        }

        try {
            MyUser userToDelete = userOptional.get();
            userRepository.delete(userToDelete);
            return ResponseEntity.ok(userToDelete);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<List<MyUser>> deleteAllUsers() {
        try {
            userRepository.deleteAll();
            return ResponseEntity.ok(new ArrayList<>());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}