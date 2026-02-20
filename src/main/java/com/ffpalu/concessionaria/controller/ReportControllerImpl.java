package com.ffpalu.concessionaria.controller;

import com.ffpalu.concessionaria.controller.interfaces.ReportController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReportControllerImpl implements ReportController {

	private final JobOperator jobOperator;
	private final Job monthlyRevenueJob;


	@Override
	public ResponseEntity<String> triggerJobMonthlyRevenue() {

		try {
			JobParameters params = new JobParametersBuilder()
							.addString("runDate", LocalDate.now().toString())
							.toJobParameters();

			log.info("Monthly Revenue Job Started");
			JobExecution execution = jobOperator.start(monthlyRevenueJob, params);

			return ResponseEntity.ok("Job started successfully " + execution.getStatus());

		}catch (JobExecutionAlreadyRunningException exception) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Job already running");
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}



	}
}
