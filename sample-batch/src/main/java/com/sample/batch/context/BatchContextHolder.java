package com.sample.batch.context;

public class BatchContextHolder {

    private static final BatchContext CONTEXT = new BatchContext();

    /**
     * 컨텍스트를 반환한다.
     *
     * @return
     */
    public static BatchContext getContext() {
        return CONTEXT;
    }
}
