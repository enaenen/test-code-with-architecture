package com.example.demo.common.infrastructure;

import com.example.demo.common.service.UuidHolder;
import java.util.UUID;

public class SystemUuidHolder implements UuidHolder {

	@Override
	public String random() {
		return UUID.randomUUID().toString();
	}
}
