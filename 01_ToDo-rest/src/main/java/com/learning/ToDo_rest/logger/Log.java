package com.learning.ToDo_rest.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Log {
    public static final Logger logger = LoggerFactory.getLogger(Log.class);
}
