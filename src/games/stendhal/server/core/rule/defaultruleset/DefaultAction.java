package games.stendhal.server.core.rule.defaultruleset;

import org.apache.log4j.Logger;


public class DefaultAction {
	
	/** the logger instance. */
	private static final Logger logger = Logger.getLogger(DefaultAction.class);
	
	/** Action class. */
	private String clazz;

	/** Action name. */
	private String name;
	
	/** Action type. */
	private String type;
	
	
	/** Creates a default action. */
	public DefaultAction(final String clazz, final String name, final String type) {
		this.clazz = clazz;
		this.name = name;
		this.type = type;
	}
	
	

}
