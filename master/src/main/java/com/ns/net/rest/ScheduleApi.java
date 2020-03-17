package com.ns.net.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotEmpty;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RequestMapping("/schedule")
public class ScheduleApi {

    @Autowired
    private ScheduleService scheduleService;

    @PutMapping("/jobs/schedule")
    public ResponseEntity<Void> scheduleJob(@NotEmpty(message = "任务ID不能为空") Long id) {
        scheduleService.scheduleJob(id);
        return ok().build();
    }

}
