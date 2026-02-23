package com.ffpalu.concessionaria.batch;

import com.ffpalu.concessionaria.dto.support.SellerMonthlyRevenue;
import com.ffpalu.concessionaria.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
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

		int rowIndex=0, collumIndex=0;


		LocalDate lastMonth = LocalDate.now();


		List<SellerMonthlyRevenue> revenues = saleRepository.findMonthlyRevenueBySeller(lastMonth.getMonthValue(),lastMonth.getYear());

		log.info("Revenues information" + revenues.toString());

		Path excelFilePath = Path.of(System.getProperty("java.io.tmpdir"), "sales_" + lastMonth.getYear() + "_" + lastMonth.getMonthValue()+".xlsx");


		try {


			XSSFWorkbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Sales Report");
			Row row = sheet.createRow(rowIndex);
			String[] headerStrings = new String[]{"Employee code", "First Name", "Last Name", "Sales Amount"};
			Cell cell;

			for (String headerField : headerStrings){

				cell = row.createCell(collumIndex);
				cell.setCellValue(headerField);
				collumIndex++;

			}

			rowIndex++;

			for (SellerMonthlyRevenue revenue : revenues) {

				collumIndex = 0;

				row = sheet.createRow(rowIndex);


				cell =  row.createCell(collumIndex);
				cell.setCellValue(revenue.getEmployeeCode());
				collumIndex++;

				cell = row.createCell(collumIndex);
				cell.setCellValue(revenue.getFirstName());
				collumIndex++;

				cell = row.createCell(collumIndex);
				cell.setCellValue(revenue.getLastName());
				collumIndex++;

				cell = row.createCell(collumIndex);
				cell.setCellValue(revenue.getTotalRevenue());
			}

			FileOutputStream outputStream = new FileOutputStream(excelFilePath.toFile());
			workbook.write(outputStream);
			workbook.close();
			outputStream.flush();
			outputStream.close();


			chunkContext
							.getStepContext()
							.getStepExecution()
							.getJobExecution()
							.getExecutionContext()
							.putString("xlsFilePath", excelFilePath.toString());

			return RepeatStatus.FINISHED;

		} catch (Exception e) {

			log.error("Error while writing csv file", e);
			throw new RuntimeException(e);

		}

	}
}
