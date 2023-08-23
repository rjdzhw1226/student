package com.student.controller;

import com.student.Constant.RedisKey;
import com.student.pojo.*;
import com.student.pojo.dto.studentDto;
import com.student.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/queryAll")
    public page<teacher> All(@RequestBody pageBean page){
        page<teacher> teacherList = teacherService.findAll(page);
        return teacherList;
    }

    @RequestMapping("/add")
    public page<teacher> Add(){
        return null;
    }

    @RequestMapping("/querySub/{id}")
    public List<student> SubAll(@PathVariable("id") String id){
        return teacherService.querySub(id);
    }

    @RequestMapping("/map")
    public String mapMine(@RequestBody loadtion load){
        try{
            stringRedisTemplate.opsForGeo().add(RedisKey.MAP_KEY + load.getUsername(),new Point(load.getX(),load.getY()), load.getUsername());
        }catch (Exception e){
            e.printStackTrace();
            return "添加失败";
        }
        return "添加成功";
    }
}
