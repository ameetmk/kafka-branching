package com.amk.demo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
@Slf4j
public class TestProducer implements ApplicationRunner {
    private final KafkaBindings binding;


    @Override
    public void run(ApplicationArguments args) throws Exception {

        Runnable runnable = () -> {
            KafkaBootStreamsConfiguration.Test test = new KafkaBootStreamsConfiguration.Test(""+System.currentTimeMillis(), new ArrayList<>(), System.currentTimeMillis());
            Message<KafkaBootStreamsConfiguration.Test> message = MessageBuilder
                    .withPayload(test)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, test.getKey().getBytes())
                    .build();
            try {
                this.binding.input().send(message);
                this.log.info("sent " + test);
            }
            catch (Exception e) {
                log.error("Error sending", e);
            }
        };
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(runnable, 1, 2, TimeUnit.SECONDS);
    }

    private static <T> T random(List<T> ts) {
        return ts.get(new Random().nextInt(ts.size()));
    }
}