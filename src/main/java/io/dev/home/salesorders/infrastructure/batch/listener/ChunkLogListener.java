package io.dev.home.salesorders.infrastructure.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

import java.time.Duration;
import java.time.Instant;


public class ChunkLogListener implements ChunkListener {

    private static final Logger logger = LoggerFactory.getLogger(ChunkLogListener.class);

    private Instant start;

    @Override
    public void beforeChunk(ChunkContext context) {
        start = Instant.now();
    }

    @Override
    public void afterChunk(ChunkContext context) {
        Instant stop = Instant.now();
        int count = context.getStepContext().getStepExecution().getReadCount();
        logger.info("chunk processed in {}, {} items", Duration.between(start, stop), count);
    }

    @Override
    public void afterChunkError(ChunkContext context) {
    }
}
