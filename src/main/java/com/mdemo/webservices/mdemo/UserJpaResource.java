package com.mdemo.webservices.mdemo;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class UserJpaResource {

    private UserDaoService service;
    private PostRepository postRepository;
    private UserRepository userRepository;
    public UserJpaResource(UserDaoService service, UserRepository userRepository, PostRepository postRepository) {
        this.service = service;
        this.userRepository=userRepository;
        this.postRepository=postRepository;
    }

    // GET /users
    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {
        //return service.findAll();
        return userRepository.findAll();
    }

    //     GET /users
    @GetMapping("/jpa/users/{id}")
    public User retrieveUser(@PathVariable int id) {
        //User user= (User) userRepository.findById(id).orElse(null);
        User user= (User) userRepository.findById(id).orElse(null);
        if(user==null)
            throw new UserNotFoundException("Id :"+id);
        return user;
    }
    @PostMapping("/jpa/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user)
    {
        User savedUser=service.save(user);
        URI location= ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/jpa/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable int id) {
//        service.deleteById(id);

      userRepository.deleteById(id);
    }
    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retrievePostsForUser(@PathVariable int id) {


        Optional<User> user=userRepository.findById(id);
        if(user.isEmpty())
            throw new UserNotFoundException("Id-"+id);
        return user.get().getPosts();
    }

    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Object> createPostForUser(@PathVariable int id, @Valid @RequestBody Post post) {


        Optional<User> user=userRepository.findById(id);
        if(user.isEmpty())
            throw new UserNotFoundException("Id-"+id);

        post.setUser(user.get());
        Post savedPost=postRepository.save(post);

        URI location=ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();
        return  ResponseEntity.created(location).build();

    }


}
