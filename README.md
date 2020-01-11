# SimpleJavaCrud

#### Extremely simple crud implementation in Java using GUI.

This is a simple Java class that performs CRUD operations on a MySQL database.

Assumptions:
1. The MySQL Java Connector is properly included in the project's libraries
2. Will connect to host:localhost at default MySQL port 3306.
3. Does not use SSL connection to the database

When run, the applet will prompt the user for their MySQL username/password (the user must have create database privileges). It will then create the database (sample_database) and corresponding table (staff), if they don't already exist.

For all crud operations, you must specify the record id.

That's it! Doesn't get much simpler than that.

Comments, suggestions, issues, and pull requests are always welcome.
