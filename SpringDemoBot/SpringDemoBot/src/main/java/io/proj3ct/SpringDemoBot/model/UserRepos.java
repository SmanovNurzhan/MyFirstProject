package io.proj3ct.SpringDemoBot.model;

import org.springframework.data.repository.CrudRepository;

public interface UserRepos extends CrudRepository<User,Long> {

}
