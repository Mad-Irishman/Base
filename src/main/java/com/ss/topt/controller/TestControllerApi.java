package com.ss.topt.controller;

import com.ss.config.jwt.JwtUtils;
import com.ss.topt.service.ActionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestControllerApi {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private ActionService actionService;


    @GetMapping("/")
    public void test(HttpServletRequest request) {
        String sessionId = jwtUtils.getSessionId()
                .orElseThrow(RuntimeException::new);
        String userId = jwtUtils.getUserId()
                .orElseThrow(RuntimeException::new);
        Long actionId = actionService.createAction(sessionId, userId, "create new tender");
        request.setAttribute(Contr.REQ_ACTION_ID, actionId);

        //основная логика
        actionService.closeAction(actionId);

    }


}
