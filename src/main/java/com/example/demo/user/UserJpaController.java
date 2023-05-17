package com.example.demo.user;

import com.example.demo.post.Post;
import com.example.demo.post.PostNotFoundException;
import com.example.demo.post.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RequestMapping("/jpa")
@RestController
public class UserJpaController {

    // 간단한 crud라서 서비스 계층 통합
    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;


    // 목록 조회
    @GetMapping("/users")
    public List<UserDto> retrieveAllUsers() {
       return userRepository.findAll().stream().map(UserDto::of).collect(Collectors.toList());
    }

    // 유저 개별 조회 with HATEOAS
    @GetMapping("/users/{id}")
    public Resource<User> retrieveUser(@PathVariable Integer id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("ID[$%s] not found", id)));

        Resource<User> resource = new Resource<>(user);
        ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        resource.add(linkTo.withRel("all-users"));
        return resource;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id) throws UserNotFoundException {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(String.format("ID[$%s] not found", id));
        }
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable Integer id) throws UserNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("ID[$%s] not found", id)));
        return user.getPosts();
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable Integer id, @Valid @RequestBody Post post) throws UserNotFoundException {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("ID[$%s] not found", id)));

        post.setUser(user);
        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}/posts/{post_id}")
    public void deletePost(@PathVariable int id, @PathVariable int post_id) throws UserNotFoundException, PostNotFoundException {
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("ID[$%s] not found", id)));

        try {
            postRepository.deleteById(post_id);
        } catch (EmptyResultDataAccessException e) { // 추상화된 DataAccessException의 하위 예외
            throw new PostNotFoundException(String.format("ID[$%s] not found", id));
        }
    }

}
