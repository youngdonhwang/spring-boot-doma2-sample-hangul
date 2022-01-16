package com.sample.batch.context;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 배치 처리 컨텍스트
 */
@Setter
@Getter
@NoArgsConstructor
public class BatchContext {

    String batchId;

    String batchName;

    LocalDateTime startDateTime;

    private final AtomicLong processCount = new AtomicLong(0);

    private final AtomicLong errorCount = new AtomicLong(0);

    private final AtomicLong totalCount = new AtomicLong(0);

    // 추가정보（임의의 객체를 전파시키고 싶을 경우를 위해）
    private final Map<String, Object> additionalInfo = new ConcurrentHashMap<>();

    private static final Object lock = new Object();

    /**
     * 배치ID와 배치명을 설정한다.
     *
     * @param batchId
     * @param batchName
     * @param localDateTime
     */
    public void set(String batchId, String batchName, LocalDateTime localDateTime) {
        synchronized (lock) {
            this.batchId = batchId;
            this.batchName = batchName;
            this.startDateTime = localDateTime;
        }
    }

    /**
     * 처리 건수를 가산한다.
     */
    public void increaseProcessCount() {
        processCount.incrementAndGet();
    }

    /**
     * 처리 건수를 반환한다.
     * 
     * @return
     */
    public long getProcessCount() {
        return processCount.intValue();
    }

    /**
     * 오류 건수를 가산한다.
     */
    public void increaseErrorCount() {
        errorCount.incrementAndGet();
    }

    /**
     * 오류 건수를 반환한다.
     * 
     * @return
     */
    public long getErrorCount() {
        return errorCount.intValue();
    }

    /**
     * 대상 건수를 가산한다.
     */
    public void increaseTotalCount() {
        totalCount.incrementAndGet();
    }

    /**
     * 대상 건수를 반환한다.
     * 
     * @return
     */
    public long getTotalCount() {
        return totalCount.intValue();
    }

    /**
     * 보유하고 있는 정보를 클리어한다.
     */
    public void clear() {
        synchronized (lock) {
            batchId = null;
            batchName = null;
            startDateTime = null;
            processCount.set(0);
            errorCount.set(0);
            totalCount.set(0);
            additionalInfo.clear();
        }
    }
}
