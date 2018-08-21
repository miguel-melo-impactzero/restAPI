package com.impactzero.restAPI.Controllers;

import com.impactzero.restAPI.Classes.User;
import com.impactzero.restAPI.Repositories.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping(path="/user")
public class UserController{


    @Autowired
    private UserRepository userRepository;


    @PostMapping(path="/add")
    public @ResponseBody User add(

            @RequestParam String name,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email

    ) throws Exception {

        User current = new User(name,username,password,email);

        try{
            userRepository.save(current);
        }catch(Exception e){
            throw new Exception(e.getClass().toString() + " : " + e.getMessage());
        }
        return userRepository.findByUsername(username);

    }

    @PutMapping(path="/update")
    public @ResponseBody String update(

            @RequestParam(value = "id") long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "email", required = false) String email

    ){

        User current;
        List<User> list;
        boolean usernameViolation = false;
        boolean emailViolation = false;

        try{

            //Get the data of the user with the id parameter
            current = userRepository.findById(id).get();

            if(current.equals(new User(id,name,username,password,email),new String("update"))){
                return "Não existem alterações nos dados do utilizador.";
            }

            //Check the repository for users that have the same username or email from the function parameters
            list = userRepository.checkConstrains(username,email);

        }catch(Exception e){
            return e.getClass().toString() + " : " + e.getMessage();
        }

        //If there are items on the list, lets check what constraint is violated.
        //If the User being updated is on the list then we don't need to check its values( won't cause SQL exceptions ).
        //If there are other Users other than the User that is being updated then we will have Constrains violations
        //therefore
        if(list.size() > 0){

            for (User u:list) {

                if(u.getId()!= id) {

                    if (u.getUsername().equals(username)) {
                        usernameViolation = true;
                    }

                    if (u.getEmail().equals(email)) {
                        emailViolation = true;
                    }

                }

            }

        }

        //Determine which constraint(s) are violated and write to String errors

        String errors = "";

        if(usernameViolation && emailViolation){

            errors = "Username e email em uso.";

        }else{

            if (usernameViolation) {

                errors = "User já existe. Deve ser unico.";

            }else{

                if(emailViolation){

                    errors = "Email em uso.";

                }
            }


        }

        if(errors.equals("")) {

            try {
                //Change the data in current to the new values if they are not null
                current.setData(name, username, password, email);
                userRepository.save(current);
            } catch(Exception e){
                return e.getClass() + " : " + e.getMessage();
            }

            return "User alterado com sucesso.";

        }else{

            return errors;

        }
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