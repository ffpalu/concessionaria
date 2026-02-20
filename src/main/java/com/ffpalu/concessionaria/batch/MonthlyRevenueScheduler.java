package com.ffpalu.concessionaria.batch;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonthlyRevenueScheduler {

	private final JobOperator jobOperator;
	private final Job monthlyRevenueJob;


	@Scheduled(cron = "0 0 8 1 * *")
	public void runMonthlyRevenueJob() throws Exception {
		JobParameters params = new JobParametersBuilder()
						.addString("runDate", LocalDate.now().toString())
						.toJobParameters();

		log.info("Monthly Revenue Job Started");
		jobOperator.start(monthlyRevenueJob, params);

	}


}
