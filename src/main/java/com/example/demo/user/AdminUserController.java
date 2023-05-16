package com.example.demo.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminUserController {

    private UserDaoService service; // 생성자 주입

    // GET /admin/users/1 -> /admin/v1/users/1
    @GetMapping("/users")
    public List<UserDto> retrieveAllUsers() {
        return service.findAll()
                .stream()
                .map(UserDto::of)
                .collect(Collectors.toList());
    }

    // GET /admin/users/1
    // HEADER Accept application/vnd.company.appv1+json
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv1+json")
    public UserDto retrieveUserV1(@PathVariable @NotNull Integer id) throws UserNotFoundException {
        return UserDto.of(service.findOne(id));
    }

    // GET /admin/users/1
    // HEADER Accept application/vnd.company.appv2+json
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv2+json")
    public UserDto retrieveUserV2(@PathVariable @NotNull Integer id) throws UserNotFoundException {
        User user = service.findOne(id);
        User userV2 = new UserV2("Gold");

        BeanUtils.copyProperties(user, userV2);
        return UserDto.of(userV2);
    }

    @PutMapping("/users/{id]}")
    public User updateById(@PathVariable Integer id, @RequestBody User user) throws UserNotFoundException {
        return service.update(id, user);
    }

}
