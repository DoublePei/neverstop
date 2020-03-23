package com.ns.net.common.util;


import com.ns.net.common.model.enums.JobCycle;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.StringJoiner;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.ns.net.common.model.enums.JobCycle.*;
import static com.ns.net.common.util.Dates.localDateTimeToStr;
import static com.ns.net.common.util.QuartzUtils.*;
import static java.util.Arrays.asList;


@Slf4j
public class CronUtils {

    public static LocalDateTime previousScheduleTimeOf(String cron, LocalDateTime sometime) {
        return preScheduleTime(cron, sometime);
    }

    public static LocalDateTime preScheduleTimeOfSomeTime(String cron, LocalDateTime someTime) {
        return preScheduleTime(cron, nextScheduleTime(cron, someTime));
    }

    public static LocalDateTime nextScheduleTimeOf(String cron, LocalDateTime sometime) {
        return nextScheduleTime(cron, sometime);
    }

    public static List<LocalDateTime> computeScheduleTimesBetween(String cron,
                                                                  LocalDateTime startTime,
                                                                  LocalDateTime endTime) {
        return computeScheduleTimes(cron, startTime, endTime);
    }

    public static long intervalOf(String cron) {
        return QuartzUtils.interval(cron);
    }

    public static JobCycle jobCycle(String cron) {
        return from(intervalOf(cron));
    }

    public static List<LocalDateTime> calTimeRange(LocalDateTime scheduleTime, String cron) {
        long interval = intervalOf(cron);

        ChronoUnit truncateUnit = truncateUnit(interval);
        Integer cycles = numberOfCycles(interval);

        LocalDateTime endTime = scheduleTime.truncatedTo(truncateUnit);
        LocalDateTime startTime = endTime.minus(cycles, truncateUnit);

        return asList(startTime, endTime);
    }

    public static List<LocalDateTime> calTimeRangeNew(LocalDateTime scheduleTime, String cron) {
        long interval = intervalOf(cron);

        ChronoUnit truncateUnit = truncateUnit(interval);

        LocalDateTime endTime = scheduleTime.truncatedTo(truncateUnit);
        LocalDateTime startTime = preScheduleTime(cron, scheduleTime).truncatedTo(truncateUnit);

        return asList(startTime, endTime);
    }

    public static String calTimeRangeStr(LocalDateTime scheduleTime, String cron) {
        return calTimeRangeStr(calTimeRangeNew(scheduleTime, cron));
    }

    public static String calTimeRangeStr(List<LocalDateTime> range) {
        checkNotNull(range, "Cal time range is null.");
        checkArgument(range.size() == 2, "Cal time range size is not 2.");

        return new StringJoiner("~", "(", "]")
                .add(localDateTimeToStr(range.get(0)))
                .add(localDateTimeToStr(range.get(1)))
                .toString();
    }
}
