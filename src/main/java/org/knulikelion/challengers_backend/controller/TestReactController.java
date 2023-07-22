package org.knulikelion.challengers_backend.controller;

import org.knulikelion.challengers_backend.service.Impl.ProjectServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestReactController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @GetMapping("/react")
    public Map<String, Object> testHandler() {
        logger.info("Test Handler 실행 됨");
        Map<String, Object> res = new HashMap<>();

        res.put("SUCCESS", true);
        res.put("SUCCESS_TEXT", "Spring Boot 연결 됨");

        return res;
    }
}
