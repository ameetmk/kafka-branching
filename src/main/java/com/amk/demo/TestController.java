package com.amk.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/*
 * This will get the current value in the Store by key. Pass in the value that is the key field in the JSON
 */
@RestController
public class TestController {
    private final Log log = LogFactory.getLog(getClass());

    private final QueryableStoreRegistry queryableStoreRegistry;

    public TestController(QueryableStoreRegistry queryableStoreRegistry) {
        this.queryableStoreRegistry = queryableStoreRegistry;
    }

	@GetMapping("test")
	public Map<String, KafkaBootStreamsConfiguration.Test> list(String key) {
		ReadOnlyKeyValueStore<String, KafkaBootStreamsConfiguration.Test> store = queryableStoreRegistry.getQueryableStoreType(KafkaBindings.MAIN_VIEW, QueryableStoreTypes.keyValueStore());

		Map<String, KafkaBootStreamsConfiguration.Test> m = new HashMap<>();
		KeyValueIterator<String, KafkaBootStreamsConfiguration.Test> iterator = store.all();
		while (iterator.hasNext()) {
			KeyValue<String, KafkaBootStreamsConfiguration.Test> next = iterator.next();
			m.put(next.key, next.value);
		}

		return  m;
	}
}
