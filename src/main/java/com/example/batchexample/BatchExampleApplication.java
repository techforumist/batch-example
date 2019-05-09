package com.example.batchexample;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableBatchProcessing
public class BatchExampleApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BatchExampleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}

}

@RestController
class HomeController {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("firstJob")
	private Job firstJob;

	@Autowired
	@Qualifier("secondJob")
	private Job secondJob;

	@GetMapping("/first")
	public String first() {
		Date dateParam = new Date();
		JobParameters param = new JobParametersBuilder()//
				.addDate("date", dateParam)//
				.toJobParameters();

		try {
			jobLauncher.run(firstJob, param);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	@GetMapping("/second")
	public String second() {
		Date dateParam = new Date();
		JobParameters param = new JobParametersBuilder()//
				.addDate("date", dateParam)//
				.toJobParameters();
		try {
			jobLauncher.run(secondJob, param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public List<String> getInput() {
		List<String> input = new ArrayList<String>();
		for (int i = 0; i < 100; i++) {
			input.add(i + " >> " + System.currentTimeMillis());
		}

		return input;
	}

}
