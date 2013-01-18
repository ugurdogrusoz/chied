package org.gvt.command;

import org.eclipse.gef.commands.Command;
import org.gvt.model.GraphObject;
import org.gvt.model.EdgeModel;
import org.gvt.model.NodeModel;

/**
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public class CreateConnectionCommand extends Command
{
	private EdgeModel connection;

	private NodeModel source, target;

	public void setConnection(Object connx)
	{
		connection = (EdgeModel) connx;
	}

	public void setSource(Object model)
	{
		source = (NodeModel) model;
	}

	public void setTarget(Object model)
	{
		target = (NodeModel) model;
	}

	/* (”ñ Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute()
	{
		connection.setSource(source);
		connection.setTarget(target);
		source.addSourceConnection(connection);
		target.addTargetConnection(connection);
	}
}