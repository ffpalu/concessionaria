package com.ffpalu.concessionaria.batch;

import com.ffpalu.concessionaria.dto.support.SellerMonthlyRevenue;
import com.ffpalu.concessionaria.repository.SaleRepository;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateCsvTasklet implements Tasklet {

	private final SaleRepository saleRepository;


	@Override
	public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		LocalDate lastMonth = LocalDate.now().minusMonths(1);

		List<SellerMonthlyRevenue> revenues = saleRepository.findMonthlyRevenueBySeller(lastMonth.getMonthValue(),lastMonth.getYear());

		Path csvPath = Path.of(System.getProperty("java.io.tmpdir"), "sales_" + lastMonth.getYear() + "_" + lastMonth.getMonthValue()+".csv");


		try {
			CSVWriter writer = new CSVWriter(Files.newBufferedWriter(csvPath));

			writer.writeNext(new String[]{"Employee code", "First Name", "Last Name", "Sales Amount"});

			revenues.forEach(revenue -> {
				writer.writeNext(
								new String[]{
												revenue.getEmployeeCode(),
												revenue.getFirstName(),
												revenue.getLastName(),
												revenue.getTotalRevenue().toString()
								}
				);
			});

			chunkContext
							.getStepContext()
							.getStepExecution()
							.getJobExecution()
							.getExecutionContext()
							.putString("csvFilePath", csvPath.toString());

			return RepeatStatus.FINISHED;

		} catch (Exception e) {

			log.error("Error while writing csv file", e);
			throw new RuntimeException(e);

		}

	}
}
