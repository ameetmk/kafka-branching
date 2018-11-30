package com.amk.demo;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

interface KafkaBindings {

    String MAIN_IN = "mainin";
    String MAIN_STREAM = "mainstream";
    String BRANCH_1 = "branch1";
    String BRANCH_2 = "branch2";

    String MAIN_VIEW = "mainvw";

    @Output(MAIN_IN)
    MessageChannel input();

    @Input(MAIN_STREAM)
    KStream<String, KafkaBootStreamsConfiguration.Test> sourceProductsIn();

    @Output(BRANCH_1)
    KStream<String, KafkaBootStreamsConfiguration.Test> branch1();

    @Output(BRANCH_2)
    KStream<String, KafkaBootStreamsConfiguration.Test> branch2();
}
