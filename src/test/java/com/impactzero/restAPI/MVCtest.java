package com.impactzero.restAPI;



import com.impactzero.restAPI.Classes.User;
import com.impactzero.restAPI.Repositories.UserRepository;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;



import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@AutoConfigureMockMvc
public class MVCtest {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void acreateUserOnDB() throws Exception {

        System.out.println("\n\n\nCREATE NEW USER---------------------------\n\n\n");

        User testuser = new User("nome","username","password","email");

        MvcResult result = mvc.perform(post("http://localhost:8080/user/add")

                                .param("name",testuser.getName())
                                .param("username",testuser.getUsername())
                                .param("password",testuser.getPassword())
                                .param("email",testuser.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)

                            )
                            //Print the request and response to the console
                            .andDo(print())
                            //We need a code 200 on the status
                            .andExpect(status().isOk())
                            //Put all the data on the MvcResult object
                            .andReturn();

        //We need to ensure the user created on the DB has the same data and declare the test as successful only if all data matches
        assertTrue(testuser.equals(new User().fromJSON(result.getResponse().getContentAsString())));

    }


    @Test
    public void bupdateUserOnDB() throws Exception {

        System.out.println("\n\n\nUPDATE NEW USER WITH SUCCESS---------------------------\n\n\n");

        //User with id = 1 already in DB, lets change his name and password so we don't new the rest of the params as all are optional
        mvc.perform(put("http://localhost:8080/user/update")

            .param("id", "1")
            .param("name","nome2")
            .param("password","password2")
            .contentType(MediaType.APPLICATION_JSON)

        )
        .andDo(print())
        .andExpect(status().isOk())
        //When a user is succefully updated the body contains a value. Check if it is there!
        .andExpect(content().string("User alterado com sucesso."))
        .andReturn();

    }

    @Test
    public void cupdateUserOnDB_WithSameData() throws Exception {

        System.out.println("\n\n\nUPDATE USER WITH SAME DATA( NO OPERATION IS DONE )---------------------------\n\n\n");

        //User with id = 1 already in DB, lets change his name and password so we don't new the rest of the params as all are optional
        mvc.perform(put("http://localhost:8080/user/update")

                .param("id", "1")
                .param("name","nome2")
                .param("password","password2")
                .contentType(MediaType.APPLICATION_JSON)

        )
        .andDo(print())
        .andExpect(status().isOk())
        //When a user is succefully updated the body contains a value. Check if it is there!
        .andExpect(content().string("Não existem alterações nos dados do utilizador."))
        .andReturn();

    }

    @Test
    public void dupdateUserOnDB_WithUsername_ConstraintViolation() throws Exception {

        System.out.println("\n\n\nCREATE NEW USER---------------------------\n\n\n");

        //Create a new user with an unique username and try changing the username of the user with id=1 to the same username of the new user
        mvc.perform(post("http://localhost:8080/user/add")

                .param("name","miguel")
                .param("username","uniqueusername")
                .param("password","password")
                .param("email","uniqueEmail")
                .contentType(MediaType.APPLICATION_JSON)

        )
        //Print the request and response to the console
        .andDo(print())
        //We need a code 200 on the status
        .andExpect(status().isOk())
        //Put all the data on the MvcResult object
        .andReturn();

        System.out.println("\n\n\nUPDATE WITH BAD USERNAME---------------------------\n\n\n");

        //User with id = 1 already in DB, lets change his username to uniqueusername to create an username constraint error
        mvc.perform(put("http://localhost/user/update")

            .param("id", "1")
            .param("username","uniqueusername")
            .contentType(MediaType.APPLICATION_JSON)

        )
        .andDo(print())
        .andExpect(status().isOk())
        //Check for the error message here
        .andExpect(content().string("User já existe. Deve ser unico."))
        .andReturn();

    }



    @Test
    public void eupdateUserOnDB_WithEmail_ConstraintViolation() throws Exception {

        System.out.println("\n\n\nUPDATE WITH BAD EMAIL---------------------------\n\n\n");

        //User with id = 1 already in DB, lets change his email to uniqueEmail to create an email constrait error
        mvc.perform(put("http://localhost/user/update")

            .param("id", "1")
            .param("email","uniqueEmail")
            .contentType(MediaType.APPLICATION_JSON)

        )
        .andDo(print())
        .andExpect(status().isOk())
        //When a user is succefully updated the body contains a value. Check if it is there!
        .andExpect(content().string("Email em uso."))
        .andReturn();

    }

    @Test
//    @DependsOn(value = "createUserOnDB")
    public void fupdateUserOnDB_WithUsername_And_Email_ConstraintViolation() throws Exception {

        System.out.println("\n\n\nUPDATE WITH BAD USERNAME AND EMAIL---------------------------\n\n\n");

        //User with id = 1 already in DB, lets change his username to uniqueusername and email to uniqueEmail to create an username and email constrait error
        mvc.perform(put("http://localhost/user/update")

            .param("id", "1")
            .param("username","uniqueusername")
            .param("email","uniqueEmail")
            .contentType(MediaType.APPLICATION_JSON)

        )
        .andDo(print())
        .andExpect(status().isOk())
        //When a user is succefully updated the body contains a value. Check if it is there!
        .andExpect(content().string("Username e email em uso."))
        .andReturn();

    }


//    @Test
//    @DependsOn(value = "createUserOnDB")
//    public void zdeleteUserOnDB() throws Exception {
//
//        mvc.perform(post("http://localhost/user/delete")
//                .param("id", "0")
//                .contentType(MediaType.APPLICATION_JSON))
//
//        .andExpect(status().isOk())
//        .andExpect(content().string("User apagado com sucesso."));
//
//    }
}
