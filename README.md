# Database-Systems

Project Details:
I.
Part 1
- Download the Yelp dataset from Camino. Look at each JSON file and understand what information the JSON objects provide. Pay attention to the data items in JSON objects that you will need for your application (For example, categories, attributes,...etc.)
- You may have to modify your database design from Homework 2 to model the database for the described application scenario on page-1. Your database schema doesn’t necessarily need to include all the data items provided in the JSON files. Your schema should be precise but yet complete. It should be designed in such a way that all queries/data retrievals on/from the database run efficiently and effectively.
- Produce DDL SQL statements for creating the corresponding tables in a relational DBMS. Note the constraints, including key constraints, referential integrity constraints, not NULL constraints, etc. needed for the relational schema to capture and enforce the semantics of your ER design.
- Populate your database with the Yelp data. Generate INSERT statements for your tables and run those to insert data into your DB.

II.
Part 2
Implement the application for searching local businesses as explained in section “Overview & Requirements”. In this milestone you would:
- Write the SQL queries to search your database.
- Establish connectivity with the DBMS.
- Embed/execute queries in/from the code. Retrieve query results and parse the returned results to generate the
output that will be displayed on the GUI.
- Implement a GUI where the user can,
  o   Search for either a business or users that match the criteria given. Please note that at any given time, the application can search for only a business or users, but not both.
  o   Browse through main, sub-categories and attributes for the businesses; select the business categories that user wants to search for; (note: The list of the main categories is given in Appendix-C. All other categories that appear in the business objects are sub-categories. Such a distinction is made for easier browsing of the business categories.)
  o   browse through the attributes of the selected categories; specify the attributes she/he is interested in; o Search for the businesses that belong to the main, sub-categories and attributes that user specifies along with review information (Fig 1) (The application should be able to search for the users that
have either all the specified attributes (AND condition) or that have any of the attributes specified
(OR condition)).
  o   Search for users with attributes shown in Fig. 2 (The application should be able to search for the
users that have either all the specified attributes (AND condition) or that have any of the attributes
specified (OR condition))
  o   Select a certain business in the search results and list all the reviews for that business. (note:
The review list should also include the names of the users who provided those reviews) o Select a user in the search results and list all the reviews for that user.
Please note that all data displayed on the GUI should be kept in the database and should be retrieved from it when needed. You are not allowed to create internal data structures to store data.


Required .sql files:
You are required to create two .sql files:
1. createdb.sql: This file should create all required tables. In addition, it should include constraints, indexes, and any
other DDL statements you might need for your application.
2. dropdb.sql: This file should drop all tables and the other objects once created by your createdb.sql file.


Required Java Programs:
You are required to implement two Java programs:
1. populate.java: This program should get the names of the input files as command line parameters and
populate them into your database. It should be executed as:
2
“> java populate yelp_business.json yelp_review.json yelp_checkin.json yelp_user.json”.
Note that every time you run this program, it should remove the previous data in your tables; otherwise the tables will have redundant data.
2. hw3.java: This program should provide a GUI, similar to figure 1, to query your database. The GUI should include:
a. List of main business categories.
b. For the Business section, list of sub-categories associated with the selected main category(ies).
c. Checkin and Review sections along with selectable attributes for business selection
d. For Users section, list of selectable attributes and dropdowns
e. List of results (Users or Businesses)
f. List of the reviews provided for a specific business/User.
