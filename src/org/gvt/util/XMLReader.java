package org.gvt.util;

import java.io.File;

import org.gvt.model.CompoundModel;

/**
 * Abstract XML Reader class for file loading
 *
 * @author Cihan Kucukkececi
 *
 * Copyright: i-Vis Research Group, Bilkent University, 2007 - present
 */
public abstract class XMLReader
{
	// if there is no location value in xml file then layout is run.
	public boolean runLayout = true;

	// margin of the graph
	int marginSize = -1;

	public abstract CompoundModel readXMLFile(File xmlFile);
}
