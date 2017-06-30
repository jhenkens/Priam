/**
 * Copyright 2013 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.priam.utils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.netflix.priam.IConfiguration;
import com.netflix.priam.scheduler.SimpleTimer;
import com.netflix.priam.scheduler.Task;
import com.netflix.priam.scheduler.TaskTimer;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Singleton
public class Log4JPropertyManager extends Task
{
	private static final Logger LOGGER = LoggerFactory.getLogger(Log4JPropertyManager.class);
    public static final String JOBNAME = "Log4j-Property-Manager";

    @Inject
    public Log4JPropertyManager(IConfiguration config)
    {
        super(config);
    }

    public void execute() throws IOException
    {
    	boolean isDone = false;
    	
    	while (!isDone) {
    	  try {
              this.reloadLog4jConfiguration(config.getLog4jProperties());
              isDone = true;
    	   } catch (Exception e) {
    		  LOGGER.error("Fail wrting cassandra.yml file. Retry again!",e);
    	   }
    	}
    	
    }

    private void reloadLog4jConfiguration(String log4jProperties) {
        if (log4jProperties == null || log4jProperties.isEmpty()) return;
        PropertyConfigurator.configure(log4jProperties);
    }

    @Override
    public String getName()
    {
        return JOBNAME;
    }

    public static TaskTimer getTimer()
    {
        return new SimpleTimer(JOBNAME);
    }
}
