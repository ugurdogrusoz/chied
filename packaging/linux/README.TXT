Chisio Editor 2.x
Copyright (c) 2009 i-Vis Research Group, Bilkent University

CONTENTS
========
I.   Summary
II.  Requirements
III. Installation
IV.  Running Chisio Editor
V.   License agreement and Copyright

I. SUMMARY
==========
This document explains how to install and run Chisio Editor 2.x in Linux.

II. REQUIREMENTS
================
Following libraries with the minimum versions given are required to be
installed and running on your Linux system:

Sun JRE 1.5

GTK  2.2.1
ATK 1.2.0
glib 2.2.1
Pango 1.2.1
Freetype 2.1.3
Cairo 1.4

Last five libraries are installed by default in a standard setup of
popular Linux distributions such as Fedora Core, Mandriva, and Ubuntu.

Make sure that "java" is on the system path. This path can be set using
the following commands:
	export JAVA_HOME={JAVA_DIR}
	export PATH=$JAVA_HOME/bin:$PATH

Above, {JAVA_DIR} corresponds to Java installation directory such as 
/opt/jdk1.5.0_09.

III. INSTALLATION
=================
No extra installation procedure is required. Simply unzip the file
chisio-2.x.x-setup-gtk-linux-x86.zip (you should have already done this to
read this file).

The zip archive should contain the following files:

lib		Core java libraries for Chisio Editor 2.x
swt		UI libraries and binaries
Chisio.sh	Batch file for running Chisio Editor 2.x
README.TXT	This file

IV. RUNNING CHISIO EDITOR 2.x
======================
Just execute Chisio.sh from a shell. Make sure that you set the
attributes of this file as executable. You can use following command:

chmod 555 Chisio.sh

V. LICENSE AGREEMENT AND COPYRIGHT
==================================
Chisio Editor 2.x is distributed under Eclipse Public License:
	http://www.eclipse.org/org/documents/epl-v10.php

