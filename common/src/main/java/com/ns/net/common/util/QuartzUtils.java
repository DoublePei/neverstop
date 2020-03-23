package com.ns.net.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.ns.net.common.util.Dates.date;
import static com.ns.net.common.util.Dates.localDateTime;
import static java.util.stream.Collectors.toList;
import static org.quartz.TriggerUtils.computeFireTimesBetween;

@Slf4j
public class QuartzUtils {

    public static boolean isSatisfiedBy(String cron, LocalDateTime scheduleTime) {
        try {
            return new CronExpression(cron).isSatisfiedBy(date(scheduleTime));
        } catch (Exception e) {
            throw new RuntimeException("Check satisfy fail: cron= " + cron + ", time=" + scheduleTime, e);
        }
    }

    public static boolean isSatisfiedBy(String cron, Date scheduleTime) throws ParseException {
        return new CronExpression(cron).isSatisfiedBy(scheduleTime);
    }

    public static LocalDateTime scheduleTime(String cronExpression, LocalDateTime someTime) {
        try {
            return localDateTime(scheduleTime(cronExpression, date(someTime)));
        } catch (Exception e) {
            throw new RuntimeException("Compute schedule time fail: cron= " + cronExpression + ", time=" + someTime, e);
        }
    }

    public static LocalDateTime nextScheduleTime(String cronExpression, LocalDateTime scheduleTime) {
        try {
            return localDateTime(nextScheduleTime(cronExpression, date(scheduleTime)));
        } catch (Exception e) {
            throw new RuntimeException("Compute next schedule time fail: cron= " + cronExpression + ", time=" + scheduleTime, e);
        }
    }

    public static LocalDateTime preScheduleTime(String cronExpression, LocalDateTime scheduleTime) {
        try {
            return localDateTime(preScheduleTime(cronExpression, date(scheduleTime)));
        } catch (Exception e) {
            throw new RuntimeException("Compute previous schedule time fail: cron= " + cronExpression + ", time=" + scheduleTime, e);
        }
    }

    public static Date scheduleTime(String cronExpression, Date someTime) throws Exception {
        CronExpression cron = new CronExpression(cronExpression);
        if (cron.isSatisfiedBy(someTime))
            return someTime;

        return cron.getNextValidTimeAfter(someTime);
    }

    public static Date nextScheduleTime(String cronExpression) throws Exception {
        return nextScheduleTime(cronExpression, new Date());
    }

    public static Date nextScheduleTime(String cronExpression, Date date) throws Exception {
        if (StringUtils.isBlank(cronExpression)) {
            return null;
        }
        CronExpression cron = new CronExpression(cronExpression);
        Date nextFireDate = cron.getNextValidTimeAfter(date);
        return nextFireDate;
    }

    public static Date preScheduleTime(String cronExpression) throws Exception {
        return preScheduleTime(cronExpression, new Date());
    }

    public static Date preScheduleTime(String cronExpression, Date date) throws Exception {
        int i = 0;
        Date next = date;
        while (true) {
            i++;
            next = nextScheduleTime(cronExpression, next);
            long interval = next.getTime() - date.getTime();
            Date pre = new Date(date.getTime() - interval);
            Date result = preScheduleTime(cronExpression, pre, date);
            if (i > 1000 || result != null) {
                if (result != null) {
                    log.debug("Cron compute pre success: cron={}, time={}, times={}", cronExpression, date, i);
                    return result;
                } else {
                    log.debug("Cron compute pre fail: cron={}, time={}, times={}", cronExpression, date, i);
                }
                break;
            }
        }
        return null;
    }

    private static Date preScheduleTime(String cronExpression, Date pre, Date date) throws Exception {
        if (pre.after(date)) {
            return null;
        }
        Date next = nextScheduleTime(cronExpression, pre);
        if (next.after(date) || next.equals(date)) {
            return null;
        }

        while (next != null && next.before(date)) {
            pre = next;
            next = preScheduleTime(cronExpression, next, date);
        }
        return pre;
    }

    public static long interval(CronExpression cron) {
        Date next = cron.getNextValidTimeAfter(new Date());
        Date nextOfNext = cron.getNextValidTimeAfter(next);

        return nextOfNext.getTime() - next.getTime();
    }

    public static long interval(String cronExpression) {
        try {
            return interval(new CronExpression(cronExpression));
        } catch (Exception e) {
            throw new RuntimeException("Get interval fail: cron=" + cronExpression, e);
        }
    }

    public static List<LocalDateTime> computeScheduleTimes(String cronExpression, LocalDateTime from, LocalDateTime to) {
        try {
            return computeFireTimesBetween(getCronTrigger(cronExpression), null, date(from), date(to))
                    .stream()
                    .map(date -> localDateTime(date))
                    .collect(toList());
        } catch (ParseException e) {
            throw new RuntimeException("Compute schedule times fail: cron=" + cronExpression
                    + ", from=" + from
                    + ", to=" + to,
                    e);
        }
    }

    public static List<Date> computeScheduleTimes(String cronExpression, Date from, Date to) throws Exception {
        return computeFireTimesBetween(getCronTrigger(cronExpression), null, from, to);
    }

    public static List<Date> computeScheduleTimes(String parentCronExpression,
                                                        String childCronExpression) throws Exception {
        return computeParentScheduleTimes(parentCronExpression, childCronExpression, new Date());
    }

    public static List<Date> computeParentScheduleTimes(String parentCronExpression,
                                                        String childCronExpression,
                                                        Date childScheduleTime) throws Exception {

        //前后都会包含
        return computeScheduleTimes(parentCronExpression,
                preScheduleTime(childCronExpression, childScheduleTime), childScheduleTime);
    }

    private static CronTriggerImpl getCronTrigger(String cron) throws ParseException {
        CronTriggerImpl cronTrigger = new CronTriggerImpl();
        cronTrigger.setCronExpression(cron);
        return cronTrigger;
    }
}
