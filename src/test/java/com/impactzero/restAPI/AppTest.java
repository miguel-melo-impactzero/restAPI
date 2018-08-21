package com.impactzero.restAPI;

import com.impactzero.restAPI.Classes.User;
import com.impactzero.restAPI.Repositories.UserRepository;
import org.h2.jdbc.JdbcSQLException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.HttpRequest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.persistence.PersistenceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AppTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testuser;
    private User newuser;
    private User invalidUsernameUser;
    private User invalidEmailUser;

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

        //Declaration of user instance
        User testuser = new User();

        //Insert the testuser into the local DB
        entityManager.persistAndFlush(testuser);

        //Change the instance variables
        testuser.setName("New testuser name");
        testuser.setUsername("New testuser username");
        testuser.setPassword("New testuser password");
        testuser.setEmail("New testuser email");

        //Insert data into the database
        entityManager.persistAndFlush(testuser);

        //Find the user inserted on the repository and instanciated another user with the data fetched
        User newuser = userRepository.findByName("New testuser name");

        //Compare both instances
        assertTrue(testuser.equals(newuser));

    }

    @Test
    public void updateWithConstraintsViolation() {

        //Declaration of 3 users : 1 valid and 2 invalid
        User testuser = new User();
        User invalidEmailUser = new User();
        User invalidUsernameUser = new User();

        try {

            //Insert the testuser into the local DB
            entityManager.persist(testuser);

            //Change the instance variables and insert all 3 users into the DB
            testuser.setName("New testuser name");
            testuser.setUsername("New testuser username");
            testuser.setPassword("New testuser password");
            testuser.setEmail("New testuser email");


            //Set duplicate fields that are unique
            invalidEmailUser.setEmail("New testuser email");
            invalidUsernameUser.setUsername("New testuser username");

            //Insert the 3 users into the DB
            entityManager.persist(testuser);
            entityManager.persist(invalidEmailUser);
            entityManager.persist(invalidUsernameUser);
            entityManager.flush();

        }catch(PersistenceException e){

            Throwable t = e.getCause();

            //Cicle through the Exception causes until a JdbcSQLException is found
            while ((t != null) && !(t instanceof JdbcSQLException)) {

                t = t.getCause();

            }

            //Check if a JdbcSQLException was found.
            //If yes then the unique constraint violation was caught successfully.
            //If no then the exception was not caught eventhough there is one, resulting in a failed test.
            if (t instanceof JdbcSQLException) {

                assertTrue(true);

            }else{

                fail();

            }

        }
    }

    @Test
    public void deleteUser(){

        //Create user and insert into DB
        User testuser = new User("To delete", "To delete", "To delete", "To delete");
        entityManager.persistAndFlush(testuser);

        //Check if user is in the DB
        assertFalse(!(testuser.equals(userRepository.findByName("To delete"))));

        //Remove user from DB
        entityManager.remove(testuser);

        //If user was deleted, user repo has no user with that name, returning null
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