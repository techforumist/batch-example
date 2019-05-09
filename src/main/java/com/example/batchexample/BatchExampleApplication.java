package com.example.batchexample;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
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
	private Job job;

	@GetMapping("/")
	public String getOne() {
		JobParameters jobParameters = new JobParameters();
		try {
			jobLauncher.run(job, jobParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
