package com.example.demo.user;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDaoService { // dao에 가시적인 역할이 없어 통합

    private static int usersCount = 3;

    private static List<User> users = new ArrayList<>(); // db 대신 상태를 저장

    static {
        users.add(new User(1, "Kenneth", new Date()));
        users.add(new User(2, "Alice", new Date()));
        users.add(new User(3, "Elena", new Date()));
    }

    public List<User> findAll() {
        return users;
    }

    // 예제 코드가 null을 반환하길래 null safe하게 바꿨습니다
    public User findOne(int id) throws UserNotFoundException {
        Optional<User> user = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
        return user.orElseThrow(() -> new UserNotFoundException(String.format("ID[$%s] not found", id)));
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++usersCount);
        }
        users.add(user);
        return user;
    }

    public User delete(Integer id) throws UserNotFoundException {
        Optional<User> user = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
        user.ifPresent(users::remove);
        return user.orElseThrow(() -> new UserNotFoundException(String.format("ID[$%s] not found", id)));
    }


    public User update(Integer id, User updatedUser) throws UserNotFoundException {
        Optional<User> user = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
        user.ifPresent(u -> {
            users.remove(u);
            users.add(updatedUser);
        });
        return user.orElseThrow(() -> new UserNotFoundException(String.format("ID[$%s] not found", id)));
    }
}
