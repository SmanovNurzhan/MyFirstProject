package io.proj3ct.SpringDemoBot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.glassfish.grizzly.http.util.TimeStamp;

@Entity(name="userDataTable")
@Data
public class User{
    @Id
    private long chatId;

    private String first_name;
    private String last_name;
    private String user_name;
    private TimeStamp registeredTimeofUser;
}
