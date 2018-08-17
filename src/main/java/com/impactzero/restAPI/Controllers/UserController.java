package com.impactzero.restAPI.Controllers;

import com.impactzero.restAPI.Classes.User;
import com.impactzero.restAPI.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/user")
public class UserController{


    @Autowired
    private UserRepository userRepository;


    @PostMapping(path="/add")
    public @ResponseBody String add(

            @RequestParam String name,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email

    ) {

        User current = new User(name,username,password,email);

        try{
            userRepository.save(current);
        }catch(Exception e){
            return e.getClass().toString() + " : " + e.getMessage();
        }

        return "User guardado com sucesso.";

    }

    @PutMapping(path="/update")
    public @ResponseBody String update(

            @RequestParam(value = "id") long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "email", required = false) String email

    ) {

        User current = new User();

        try{

            //Get the data of the user with the id parameter
            current = userRepository.findById(id).get();


            List<User> list = userRepository.checkConstrains(current.getUsername(),current.getEmail());


            boolean usernameViolation = false;
            boolean emailViolation = false;

            if(list.size() > 0){


                for (User u:list) {

                    if(u.getUsername().equals(username)){
                        usernameViolation = true;
                    }

                    if(u.getEmail().equals(email)){
                        emailViolation = true;
                    }

                }

            }

            if(usernameViolation && emailViolation){

                throw new Exception("Username e email em uso.");

            }else{

                if (usernameViolation) {

                    throw new Exception("User já existe. Deve ser unico.");

                }else{

                    if(emailViolation){

                        throw new Exception("Email em uso.");

                    }
                }


            }

        }catch(Exception e){
            return e.getClass().toString() + " : " + e.getMessage();
        }

        try {
            //Change the data in current to the new values if they are not null
            current.setData(name,username,password,email);
            userRepository.save(current);
        } catch (Exception e) {
            return e.getClass().toString() + " : " + e.getMessage();
        }

        return "User alterado com sucesso.";

    }

    @PostMapping(path="/delete")
    public @ResponseBody String delete(

            @RequestParam(value = "id") long id

    ) {

        try {
            userRepository.deleteById(id);
        }catch(Exception e){
            return e.getClass().toString() + " : " + e.getMessage();
        }
        return "User apagado com sucesso.";
    }

    @GetMapping(path="/select")
    public @ResponseBody Iterable<User> select() {

        return userRepository.findAll();

    }

}