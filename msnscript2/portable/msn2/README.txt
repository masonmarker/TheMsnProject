This directory ("portable/msn2") can now be copied into
your project folders / directories.

The destination folder environment of this package
should have access to the following:
- capability to execute .py programs
- installed the few dependencies in dependencies.txt

You should create your .msn2 program files within the same directory
as this README.txt ("portable/msn2").

This package has access to an MSN2 library,
therefore, your created .msn2 program files can import files
in the exact manner of those existing outside of this package.

ex: import ("lib/timer.msn2")

Run .msn2 programs from this directory with the following syntax:
"python msn2.py *****.msn2"

Good luck!