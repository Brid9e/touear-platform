package com.touear.core.log.listener;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;




/**
 * logback监听类
 *
 * @author Chenl
 */
public class LoggerStartupListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {

	@Override
	public void start() {
		Context context = getContext();
		context.putProperty("ELK_MODE", "FALSE");
		context.putProperty("STDOUT_APPENDER", "STDOUT");
		context.putProperty("INFO_APPENDER", "INFO");
		context.putProperty("ERROR_APPENDER", "ERROR");
		context.putProperty("DESTINATION", "127.0.0.1:9000");

	}

	@Override
	public void stop() {

	}

	@Override
	public boolean isStarted() {
		return false;
	}

	@Override
	public boolean isResetResistant() {
		return false;
	}

	@Override
	public void onStart(LoggerContext context) {

	}

	@Override
	public void onReset(LoggerContext context) {

	}

	@Override
	public void onStop(LoggerContext context) {

	}

	@Override
	public void onLevelChange(Logger logger, Level level) {

	}
}
