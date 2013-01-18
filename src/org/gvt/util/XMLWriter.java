package org.gvt.util;

import org.gvt.model.CompoundModel;

/**
 * Abstract XML Writer class for file saving
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public abstract class XMLWriter
{
	// margin of the graph
	int marginSize = -1;

	public abstract Object writeXMLFile(CompoundModel root);
}
