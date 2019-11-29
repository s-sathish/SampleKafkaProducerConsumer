package com.sathish.KPC.producer;

import org.springframework.cloud.stream.binder.PartitionSelectorStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CustomPartitioner {

    @Bean
    public Object CustomPartitioner1() {
        return new PartitionSelectorStrategy() {
            @Override
            public int selectPartition(Object key, int partitionCount) {
                return 0;
            }
        };
    }

    @Bean
    public Object CustomPartitioner2() {
        return new PartitionSelectorStrategy() {
            @Override
            public int selectPartition(Object key, int partitionCount) {
                return 1;
            }
        };
    }

    @Bean
    public Object CustomPartitioner3() {
        return new PartitionSelectorStrategy() {
            @Override
            public int selectPartition(Object key, int partitionCount) {
                return 2;
            }
        };
    }
}