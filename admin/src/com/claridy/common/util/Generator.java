package com.claridy.common.util;

import java.text.DecimalFormat;
import java.util.Stack;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class Generator {
	private Stack stack;
	private Long next;
	public Generator() {
		stack = new Stack();
	}

	private void produce(Session session) {
		String sql="select OPUS_SEQ.nextval from dual";
		try { 
			/*PreparedStatement st = conn.prepareStatement(sql); 
			ResultSet rs = st.executeQuery(); 
			if ( rs.next() ) { 
				next = rs.getLong(1); 
			} 
			else { 
				next = 1l;
			} */
			Query query = session.createSQLQuery(sql);
			Object obj = query.uniqueResult();
			if(obj!=null&&!"".equals(obj)){
				next=Long.parseLong(obj.toString());
			}
		}catch(Exception e) { 
			throw new HibernateException(e); 
		} 
		finally { 

		}
		String key ="OPUS"+(new DecimalFormat("00000").format((long) next));
		stack.push(key);
	}

	public String get(Session session) {
		if (stack.empty()) {
			produce(session);
		}
		return (String) stack.pop();
	}
}
