package com.impactzero.restAPI;

import com.impactzero.restAPI.Classes.User;
import com.impactzero.restAPI.Repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AppTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testuser;
    private User newuser;

    @Test
    public void createUser(){

        User testuser = new User("Utilizador de teste","uTest","4444","uTest@impactzero.pt");
        entityManager.persist(testuser);
        entityManager.flush();

        User newuser = userRepository.findByName("Utilizador de teste");

        assertTrue(testuser.getName().equals(newuser.getName()));

    }

    @Test
    public void updateUser(){

//        try{

            //Declaration of 3 users : 1 valid and 2 invalid
            User testuser = new User();
            User invalidEmailUser = new User();
            User invalidUsernameUser = new User();

            //Set duplicate fields that are unique
//            invalidEmailUser.setEmail("New testuser email");
//            invalidUsernameUser.setUsername("New testuser username");

            //Insert the testuser into the local DB
            entityManager.persistAndFlush(testuser);

            //Change the instance variables and insert all 3 users into the DB
            testuser.setName("New testuser name");
            testuser.setUsername("New testuser username");
            testuser.setPassword("New testuser password");
            testuser.setEmail("New testuser email");

            entityManager.persist(testuser);
            entityManager.persist(invalidEmailUser);
            entityManager.persist(invalidUsernameUser);

            entityManager.flush();

//        }catch(PersistenceException e){

//            Throwable t = e.getCause();
//            while ((t != null) && !(t instanceof ConstraintViolationException)) {
//                t = t.getCause();
//            }
//            if (t instanceof ConstraintViolationException) {
//                assertTrue(true);
//            }
//        }




        User newuser = userRepository.findByName("New testuser name");

        assertTrue(testuser.equals(newuser) &&
                             newuser.getName().equals("New testuser name") &&
                             newuser.getUsername().equals("New testuser username") &&
                             newuser.getPassword().equals("New testuser password") &&
                             newuser.getEmail().equals("New testuser email")
        );

    }

    @Test
    public void deleteUser(){

        User testuser = new User("To delete", "To delete", "To delete", "To delete");
        entityManager.flush();
        entityManager.remove(testuser);

        assertNull(userRepository.findByName("To delete"));

    }

    @Test
    public void selectAll(){

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        User user4 = new User();

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.persist(user4);
        entityManager.flush();

        List<User> listuser = new ArrayList<User>();
        listuser.add(user1);
        listuser.add(user2);
        listuser.add(user3);
        listuser.add(user4);

        assertTrue( userRepository.findAll().equals(listuser));

    }

}