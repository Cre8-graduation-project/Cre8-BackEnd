package com.gaduationproject.cre8.apitest.controller;

import com.gaduationproject.cre8.apitest.domain.ApiTest;
import com.gaduationproject.cre8.apitest.domain.RedisTest;
import com.gaduationproject.cre8.apitest.dto.TestRequestDto;
import com.gaduationproject.cre8.apitest.repository.RedisTestRepository;
import com.gaduationproject.cre8.apitest.repository.TestRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiTestController {
    private final TestRepository testRepository;
    private final RedisTestRepository redisTestRepository;

    @PostMapping("/test")
    public void saveTest(@RequestBody ApiTest apiTest){
        testRepository.save(apiTest);
    }

    @GetMapping("/test")
    public List<ApiTest> showTestList(){
        return testRepository.findAll();
    }

    @PostMapping("/redis/test")
    public void redisSaveTest(@RequestBody TestRequestDto testRequestDto){
        redisTestRepository.save(RedisTest.builder().redisName(testRequestDto.getRedisName()).build());
    }

    @GetMapping("/redis/test")
    public List<RedisTest> redisShowTest(){
        return (List<RedisTest>) redisTestRepository.findAll();
    }



}
