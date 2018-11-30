package com.amk.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class KafkaBootStreamsConfiguration {

    @StreamListener(KafkaBindings.MAIN_STREAM)
    @SendTo({KafkaBindings.BRANCH_1, KafkaBindings.BRANCH_2})
	public KStream<String, Test>[] process(KStream<String, Test> input) {

        Predicate<String, Test> even = (key, value) -> {

            boolean val = ((value.created % 2) == 0);

            if(val) {
                System.out.println(" this message should goto even ============ key = "+ key +", value: "+ value);
            }
            return val;
        };
        Predicate<String, Test> odd = (key, value) -> {
            boolean val = ((value.created % 2) != 0);

            if(val) {
                System.out.println(" this message should goto odd ============ key = "+ key +", value: "+ value);
            }
            return val;
        };

        return input.branch(even, odd);


//		KTable<String, Test> combinedDocuments = input
//				.map(new TestKeyValueMapper())
//				.groupByKey()
//				.reduce(new TestReducer(), Materialized.<String, Test, KeyValueStore<Bytes, byte[]>>as("streams-json-store"))
//	    		;
//
//	        return combinedDocuments.toStream();
	        
    }


    @KafkaListener(topics = "mainin", groupId = "ameet")
    public void listen(Object test) {
        log.info("Received Messasge in group foo: " + test);
    }
	
    public static class TestKeyValueMapper implements KeyValueMapper<String, Test, KeyValue<String, Test>> {

		@Override
		public KeyValue<String, Test> apply(String key, Test value) {
			return new KeyValue<String, KafkaBootStreamsConfiguration.Test>(value.getKey(), value);
		}
		
    }
    
    public static class TestReducer implements Reducer<Test> {

		@Override
		public Test apply(Test value1, Test value2) {
			value1.getWords().addAll(value2.getWords());
			return value1;
		}
    	
    }
    
    @Data
	@AllArgsConstructor
    public static class Test {
    	
    	private String key;
    	private List<String> words;
    	long created;

    	public Test() {
    		words = new ArrayList<>();
    	}
    	
    }
    
}
