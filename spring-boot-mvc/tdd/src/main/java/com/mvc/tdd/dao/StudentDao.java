package com.mvc.tdd.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mvc.tdd.entity.CollegeStudent;

@Repository
public interface StudentDao extends CrudRepository<CollegeStudent, Integer> {

    // ERROR: Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'studentDao' defined in com.mvc.tdd.dao.StudentDao defined in @EnableJpaRepositories declared on JpaRepositoriesRegistrar.EnableJpaRepositoriesConfiguration: Invocation of init method failed; nested exception is org.springframework.data.repository.query.QueryCreationException: Could not create query for public abstract com.mvc.tdd.entity.CollegeStudent com.mvc.tdd.dao.StudentDao.findByEmailAddress
    // RESOLVED:
    // Jpa required entity property need to matched with defined method in Repository
    // E.g: "email" needs to match with findByEmail (findByEmailAddress will lead to above exception)
    public CollegeStudent findByEmail(String email);
}
