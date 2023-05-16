package com.example.demo.user;

import lombok.AllArgsConstructor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@AllArgsConstructor
@RestController
public class UserController {

    private UserDaoService service; // 생성자 주입

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public Resource<User> retrieveUser(@PathVariable Integer id) throws UserNotFoundException {
        User user = service.findOne(id);
        Resource<User> resource = new Resource<>(user);
        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        resource.add(linkTo.withRel("all-users"));
        return resource;
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@Valid @RequestBody User user) { // 역직렬화
        User savedUser = service.save(user);

        // 새로 추가한 user의 location
        // path : /users/{id}
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        // ResponseEntity의 BodyBuilder
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public User deleteById(@PathVariable Integer id) throws UserNotFoundException {
        return service.delete(id);
    }

    @PutMapping("/users/{id]}")
    public User updateById(@PathVariable Integer id, @RequestBody User user) throws UserNotFoundException {
        return service.update(id, user);
    }

}
