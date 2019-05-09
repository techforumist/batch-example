package com.example.batchexample.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class JobConfiguration {

	@Autowired(required = true)
	public JobBuilderFactory jobBuilderFactory;

	@Autowired(required = true)
	public StepBuilderFactory stepBuilderFactory;

	List<String> input = new ArrayList<String>();

	public JobConfiguration() {
		for (int i = 0; i < 100; i++) {
			input.add(i + " >> " + System.currentTimeMillis());
		}
	}

	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("spring_batch");
		asyncTaskExecutor.setConcurrencyLimit(3);
		return asyncTaskExecutor;
	}

	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
		return jobBuilderFactory//
				.get("importUserJob")//
				.incrementer(new RunIdIncrementer())//
				.listener(listener)//
				.flow(step1)//
				.end()//
				.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")//
				.<String, String>chunk(3)//
				.reader(reader())//
				.processor(processor())//
				.writer(writer())//
				.taskExecutor(taskExecutor())//
				.build();
	}

	private ItemWriter<String> writer() {
		return new ItemWriter<String>() {

			@Override
			public void write(List<? extends String> items) throws Exception {

				try {
					Thread.sleep(200);
				} catch (Exception e) {
					// TODO: handle exception
				}
				System.out.println(Thread.currentThread().getName() + " >> " + items.size() + " >> " + items);
			}
		};
	}

	private ItemProcessor<String, String> processor() {
		return new ItemProcessor<String, String>() {

			@Override
			public String process(String item) throws Exception {
				return item;
			}
		};
	}

	private ItemReader<String> reader() {
		return new ItemReader<String>() {
			@Override
			public String read()
					throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
				if (!input.isEmpty()) {
					return input.remove(0);
				}
				return null;
			}
		};
	}
}