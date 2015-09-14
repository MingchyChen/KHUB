package com.claridy.common.util;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.id.Configurable;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.SequenceGenerator;

public class GetGenerator extends SequenceGenerator implements PersistentIdentifierGenerator, Configurable {
	private final static Generator instance = new Generator();

	public Serializable generate(Session session) {
		return instance.get(session);
	}
}
