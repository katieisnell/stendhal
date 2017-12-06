package games.stendhal.server.core.rule.defaultruleset;

import org.apache.log4j.Logger;

import games.stendhal.client.ClientSingletonRepository;
import games.stendhal.client.actions.SlashAction;
import marauroa.common.game.RPAction;


public class DefaultAction implements SlashAction{
	
	/** the logger instance. */
	private static final Logger logger = Logger.getLogger(DefaultAction.class);
	
	/** Action type. */
	private String type;
	
	/** Parameters typed by the user. (won't be more than 30 parameters) */
	private String[] paramsKeys = new String[30];
	
	/** Remaining text typed by the user. */
	private String remainderKey;
	
	/** Minimum number of parameters required for this action */
	private int minParameters;
	
	/** Maximum number of parameters required for this action */
	private int maxParameters;
	
	/** The implementation class for this action */
	private String implementation;
	
	/** The name of the action */
	private String name;
	/** Creates a default action. */
	public DefaultAction(final String type) {
		this.type = type;
	}
	
	@Override
	public boolean execute(String[] params, String remainder) {
		final RPAction action = new RPAction();
		
		// all actions have a type
		action.put("type", this.type);
		
		// set the action parameters (if there are any parameters)
		if (params.length != 0) {
			for(int paramIndex = 0; paramIndex < params.length; ++paramIndex) {
				action.put(paramsKeys[paramIndex], params[paramIndex]);
			}
		}
		
		// set the action remainder (if there is a remainder)
		if (this.remainderKey != null) {
			action.put(this.remainderKey, remainder);
		}
		
		ClientSingletonRepository.getClientFramework().send(action);
		return true;
	}
	
	public void addParamKeyAtIndex(int index, String paramKey) {
		this.paramsKeys[index] = paramKey;
	}
	
	public void addRemainderKey(String remainderKey) {
		this.remainderKey = remainderKey;
	}
	
	public void setImplementation(String implementation) {
		this.implementation = implementation;
	}
	
	public void setMinimumParameters(String minParameters) {
		this.minParameters = Integer.parseInt(minParameters);
	}
	
	public void setMaximumParameters(String maxParameters) {
		this.maxParameters = Integer.parseInt(maxParameters);
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int getMaximumParameters() {
		return maxParameters;
	}

	@Override
	public int getMinimumParameters() {
		return minParameters;
	}	
	
	public String getType() {
		return this.type;
	}
	
	public String getImplementation() {
		return implementation;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return implementation + "@" + Integer.toHexString(hashCode());
	}
}
