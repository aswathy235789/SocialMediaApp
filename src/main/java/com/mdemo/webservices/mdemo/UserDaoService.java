package com.mdemo.webservices.mdemo;

import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Configuration
public class UserDaoService {
    private static  int userCount=0;
    private static List<User> users=new ArrayList<>();
    static {
        users.add(new User(1,"swath", LocalDate.now().minusYears(30)));
        users.add(new User(2,"sailfish", LocalDate.now().minusYears(30)));
    }
    public List<User> findAll(){
        return users;
    }

    public User findOne(int id) {
        Predicate<? super User> predicate=user->user.getId().equals(id);
        return users.stream().filter(predicate).findFirst().orElse(null);
    }
    public User save(User user)
    {
        user.setId(userCount++);
        users.add(user);
        return user;
    }

    public void deleteById(int id)
    {

        Predicate<? super User> predicate=user->user.getId().equals(id);
        users.removeIf(predicate);
        //return users.stream().filter(predicate).findFirst().orElse(null);
    }

}
