package com.mvc.tdd.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.mvc.tdd.entity.ScienceGrade;

@Repository
public interface ScienceGradesDao extends CrudRepository<ScienceGrade, Integer> {

    public Iterable<ScienceGrade> findGradesByStudentId (int id);

    public void deleteByStudentId(int id);
}
