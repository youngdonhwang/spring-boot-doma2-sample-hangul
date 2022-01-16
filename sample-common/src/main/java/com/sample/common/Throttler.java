package com.sample.common;

import java.util.concurrent.TimeUnit;

import lombok.val;

/**
 * 조절하기
 */
public class Throttler {

    private long maxRequestsPerPeriod = 0;

    private long timePeriodMillis = 1000;

    private TimeSlot timeSlot = null;

    /**
     * 생성자
     *
     * @param maxRequestsPerPeriod
     */
    public Throttler(int maxRequestsPerPeriod) {
        this.maxRequestsPerPeriod = maxRequestsPerPeriod;
    }

    /**
     * 처리를 실행한다.
     *
     * @param callback
     */
    public <T> void process(Callback<T> callback) {
        // 초간 액세스 수를 초과하는 경우, 지연시켜야 할 밀리 초 수
        long delay = this.calculateDelay();

        if (0 < delay) {
            this.delay(delay);
        }

        callback.execute();
    }

    /**
     * 지정된 밀리 초만큼 슬립시킨다.
     *
     * @param delay
     * @throws InterruptedException
     */
    protected void delay(long delay) {
        if (0 < delay) {
            try {
                TimeUnit.MICROSECONDS.sleep(delay);
            } catch (InterruptedException e) {
            }
        }
    }

    protected synchronized long calculateDelay() {
        TimeSlot slot = nextSlot();

        if (!slot.isActive()) {
            return slot.startTime - System.currentTimeMillis();
        }

        return 0;
    }

    protected synchronized TimeSlot nextSlot() {
        if (timeSlot == null) {
            timeSlot = new TimeSlot();
        }

        if (timeSlot.isFull()) {
            timeSlot = timeSlot.next();
        }

        timeSlot.assign();

        return timeSlot;
    }

    protected class TimeSlot {

        private long capacity = Throttler.this.maxRequestsPerPeriod;

        private long duration = Throttler.this.timePeriodMillis;

        private long startTime;

        protected TimeSlot() {
            this(System.currentTimeMillis());
        }

        protected TimeSlot(long startTime) {
            this.startTime = startTime;
        }

        protected synchronized void assign() {
            capacity--;
        }

        protected synchronized TimeSlot next() {
            val startTime = Math.max(System.currentTimeMillis(), this.startTime + this.duration);
            val slot = new TimeSlot(startTime);
            return slot;
        }

        protected synchronized boolean isActive() {
            return startTime <= System.currentTimeMillis();
        }

        protected synchronized boolean isFull() {
            return capacity <= 0;
        }
    }

    public interface Callback<T> {
        T execute();
    }
}
