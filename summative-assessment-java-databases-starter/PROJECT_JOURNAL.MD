# Summary

Our Database Summative Assessment involved upgrading the SimpleBistro app, originally designed with a file-based repository, to leverage a MySQL database. Using JDBC connection templates and Spring Boot, we implemented a robust and scalable solution to seamlessly integrate the app's data handling capabilities, enhancing both its performance and maintainability.

### Initial Design

Given the method signatures in the data interfaces, we immediately created a diagram to trace the relationships between the project's data tables (primary key -> foreign key). From there, we were able to divide up the workload in a way to best meet the project deadlines. The workload included the following:

- Setup project's Spring Annotations 
- Create Mapper objects to correspond with the models.
- Implement JDBC Repositories to handle DB CRUD functionality
- Create unit tests for the repository methods as they're being built to avoid headaches later.

### Challenges along the way

Having multiple people from different time zones referencing different systems and local SQL databases presents some challenges:

- Requires being conscientious of altering data tables everyone references.
- When testing, does it make more sense to create a DB double, set up mock services, or write tests that leave no trace in the real DB?
