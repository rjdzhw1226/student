package com.student.service;

import com.student.pojo.page;
import com.student.pojo.subject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface SubjectService {
    public void findByMultiThreads();

    public page<subject> query(Integer page, Integer size);

    public page<subject> queryAllSubject(Integer page, Integer size);

    public List<subject> queryAllSubjectGiveUp(Integer page, Integer size) throws ExecutionException, InterruptedException;

    public Map<String, Object> connectSubject(String subId);

    public Map<String, Object> doSubjectChoose(String subId);

    public Map<String, Object> connectSubName(String subId, String stuName);

    public page<subject> chooseSubject(Integer page, Integer size);

    public void executeChoose(String subId, String stuName);
}
