package com.spring.cloud.service.gateway.security;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PersonRestService 
{
    private static final List<Person> persons;

    static
    {
        persons = new ArrayList<>();
        persons.add(new Person("Hello", "World"));
        persons.add(new Person("Foo", "Bar"));
    }

    @RequestMapping(path = "/persons", method = RequestMethod.GET)
    public static List<Person> getPersons() {
        return persons;
    }

    @RequestMapping(path = "/persons/{name}", method = RequestMethod.GET)
    public static Person getPerson(@PathVariable("name") String name) {
        return persons.stream()
                .filter(person -> name.equalsIgnoreCase(person.getName()))
                .findAny().orElse(null);
    }
    
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public static List<User> getUsers() 
    {
    	List<User> userList = new ArrayList<>();
    	
    	User user1 = new User();
    	User user2 = new User();
    	User user3 = new User();
    	
    	user1.setId((long) 1);
    	user1.setFirstname("Rajib");
    	user1.setLastname("Garai");
    	user1.setPassword("12345");
    	user1.setEmail("rajibgarai@gmail.com");
    	
    	user2.setId((long) 2);
    	user2.setFirstname("Surojit");
    	user2.setLastname("Giri");
    	user2.setPassword("744215");
    	user2.setEmail("surojitgiri@gmail.com");
    	
    	user3.setId((long) 3);
    	user3.setFirstname("Shatarupa");
    	user3.setLastname("Sarkar");
    	user3.setPassword("25412");
    	user3.setEmail("sarkar.shatarupa@gmail.com");
    	
    	userList.add(user1);
    	userList.add(user2);
    	userList.add(user3);
    	
        return userList;
    }
}
