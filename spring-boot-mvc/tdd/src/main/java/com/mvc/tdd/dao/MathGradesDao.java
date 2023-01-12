package com.mvc.tdd.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mvc.tdd.entity.MathGrade;

@Repository
public interface MathGradesDao extends CrudRepository<MathGrade, Integer> {

    public Iterable<MathGrade> findGradesByStudentId (int id);

    public void deleteByStudentId(int id);
}
