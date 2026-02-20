package com.ffpalu.concessionaria.config;

import com.ffpalu.concessionaria.batch.GenerateCsvTasklet;
import com.ffpalu.concessionaria.batch.SendEmailTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class MonthlyRevenueJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final GenerateCsvTasklet generateCsvTasklet;
	private final SendEmailTasklet sendEmailTasklet;


	@Bean
	public Step generateCsvStep() {
		return new StepBuilder("generateCsvStep", jobRepository)
						.tasklet(generateCsvTasklet, transactionManager)
						.build();
	}

	@Bean
	public Step sendEmailStep() {
		return new StepBuilder("sendEmailStep", jobRepository)
						.tasklet(sendEmailTasklet, transactionManager)
						.build();
	}

	@Bean
	public Job monthlyRevenueJob() {
		return new JobBuilder("monthlyRevenueJob", jobRepository)
						.start(generateCsvStep())
						.next(sendEmailStep())
						.build();
	}
}
