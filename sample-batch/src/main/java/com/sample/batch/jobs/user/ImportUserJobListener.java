package com.sample.batch.jobs.user;

import com.sample.batch.listener.BaseJobExecutionListener;

public class ImportUserJobListener extends BaseJobExecutionListener {

    @Override
    protected String getBatchId() {
        return "BATCH_002";
    }

    @Override
    protected String getBatchName() {
        return "사용자 정보 데이터 로드";
    }
}
