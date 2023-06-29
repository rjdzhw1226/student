package com.student.service;

import com.student.mapper.ScoreMapper;
import com.student.pojo.score;
import com.student.pojo.vo.scoreVo;
import com.student.pojo.student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Transactional
    public List<scoreVo> queryScore(String id){
        List<scoreVo> list = new ArrayList<>();
        List<score> scores = scoreMapper.queryScore(id);
        for (score score : scores) {
            String subId = score.getSubId();
            String name = scoreMapper.querySubNameById(subId);
            scoreVo sv = new scoreVo();
            sv.setScore(score.getScore());
            sv.setName(name);
            list.add(sv);
        }
        return list;
    }

    public List<student> queryAll(){
        return null;
    }
}
