package com.hao.demo;


import io.netty.buffer.ByteBufAllocatorMetric;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocatorMetric;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.util.internal.PlatformDependent;
import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetricDemo {

    @SneakyThrows
    public static void main(String[] args) {

        UnpooledByteBufAllocator.DEFAULT.heapBuffer(1024);
        UnpooledByteBufAllocator.DEFAULT.directBuffer(1024);

        PooledByteBufAllocator.DEFAULT.heapBuffer(1024);
        PooledByteBufAllocator.DEFAULT.directBuffer(1024);

        ByteBufAllocatorMetric unpooledMetric = UnpooledByteBufAllocator.DEFAULT.metric();
        PooledByteBufAllocatorMetric pooledMetric = PooledByteBufAllocator.DEFAULT.metric();


        if (PlatformDependent.useDirectBufferNoCleaner()) {
            long usedDirectMemory = PlatformDependent.usedDirectMemory();
            log.info("DirectBuffer MemoryUsed: {} B", usedDirectMemory);

            Field field = PlatformDependent.class.getDeclaredField("DIRECT_MEMORY_COUNTER");
            field.setAccessible(true);
            log.info("DIRECT_MEMORY_COUNTER: {}", field.get(PlatformDependent.class));

        } else { // use cleaner
            BufferPoolMXBean directBufferPoolMXBean = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class).get(0);
            log.info("DirectBuffer count: {}, MemoryUsed: {}", directBufferPoolMXBean.getCount(), directBufferPoolMXBean.getMemoryUsed());
        }

        long usedUnpooledHeapMemory = unpooledMetric.usedHeapMemory();
        long usedUnpooledDirectMemory = unpooledMetric.usedDirectMemory();

        long usedPooledHeapMemory = pooledMetric.usedHeapMemory();
        long usedPooledDirectMemory = pooledMetric.usedDirectMemory();


        log.info("Used heap memory: {} B (unpooled: {}, pooled: {})",
                usedUnpooledHeapMemory + usedPooledHeapMemory, usedUnpooledHeapMemory, usedPooledHeapMemory);

        log.info("Used direct memory: {} B (unpooled: {}, pooled: {})",
                usedUnpooledDirectMemory + usedPooledDirectMemory, usedUnpooledDirectMemory, usedPooledDirectMemory);


        log.info("Number of ThreadLocalCaches: {}", pooledMetric.numThreadLocalCaches());







    }

}
