package com.example.demo.common.infrastructure;

import com.example.demo.common.service.ClockHolder;
import java.time.Clock;
import org.springframework.stereotype.Component;

@Component
public class SystemClockHolder implements ClockHolder {

	@Override
	public long mills() {
		return Clock.systemUTC().millis();
	}
}
