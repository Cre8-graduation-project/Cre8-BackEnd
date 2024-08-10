package com.gaduationproject.cre8.app.apitest.controller;

import com.gaduationproject.cre8.domain.apitest.entity.ApiTest;
import com.gaduationproject.cre8.externalApi.redis.domain.RedisTest;
import com.gaduationproject.cre8.app.apitest.dto.RedisTestRequestDto;
import com.gaduationproject.cre8.app.apitest.dto.TestRequestDto;
import com.gaduationproject.cre8.externalApi.redis.repository.RedisTestRepository;
import com.gaduationproject.cre8.domain.apitest.repository.TestRepository;
import com.gaduationproject.cre8.app.auth.interfaces.CurrentMember;
import com.gaduationproject.cre8.common.response.BaseResponse;
import com.gaduationproject.cre8.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "테스트 용도 컨트롤러", description = "배포가 잘 되었는지 테스트 합니다.")
public class ApiTestController {

    private final TestRepository testRepository;
    private final RedisTestRepository redisTestRepository;

    @PostMapping("/test")
    @Operation(summary = "test 용 저장", description = "test 에 데이터를 저장합니다. ")
    public ResponseEntity<Void> saveTest(@RequestBody @Valid final TestRequestDto testRequestDto) {
        ApiTest apiTest = ApiTest.builder().test(testRequestDto.getName()).build();
        testRepository.save(apiTest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/test")
    @Operation(summary = "test 데이터 보여주기", description = "저장된 모든 test 데이터를 보여줍니다.  ")
    public ResponseEntity<BaseResponse<List<ApiTest>>> showTestList() {
        return ResponseEntity.ok(BaseResponse.createSuccess(testRepository.findAll()));
    }

    @PostMapping("/redis/test")
    @Operation(summary = "redis test 용 저장", description = "redis test 에 데이터를 저장합니다. ")
    public ResponseEntity<Void> redisSaveTest(
            @RequestBody @Valid RedisTestRequestDto redisTestRequestDto) {
        redisTestRepository.save(
                RedisTest.builder().redisName(redisTestRequestDto.getRedisName()).build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/redis/test")
    @Operation(summary = "redis test 데이터 보여주기", description = "저장된 모든 redis test 데이터를 보여줍니다. ")
    public ResponseEntity<BaseResponse<List<RedisTest>>> redisShowTest() {
        return ResponseEntity.ok(
                BaseResponse.createSuccess((List<RedisTest>) redisTestRepository.findAll()));
    }

    @GetMapping("/test/login")
    @Operation(summary = "로그인 테스트 확인", description = "로그인 되어 있는지 판단합니다")
    public ResponseEntity<String> checkLogin(@CurrentMember Member member) {
        return ResponseEntity.ok("로그인 성공을 축하드립니다 당신의 이름은 "+member.getName());
    }


}
