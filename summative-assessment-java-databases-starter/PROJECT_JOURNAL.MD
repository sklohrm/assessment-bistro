# Summary

Our Database Summative Assessment involved upgrading the SimpleBistro app, originally designed with a file-based repository, to leverage a MySQL database. Using JDBC connection templates and Spring Boot, we implemented a robust and scalable solution to seamlessly integrate the app's data handling capabilities, enhancing both its performance and maintainability.

### Initial Design

Given the method signatures in the data interfaces, we immediately created a diagram to trace the relationships between the project's data tables (primary key -> foreign key). From there, we were able to divide up the workload in a way to best meet the project deadlines. The workload included the following:

- Setup project's Spring Annotations 
- Create Mapper objects to correspond with the models.
- Implement JDBC Repositories to handle DB CRUD functionality
- Create unit tests for the repository methods as they're being built to avoid headaches later.

### Challenges and Open Ended Questions

Various challenges encountered on this project:

- Requires being conscientious of altering data tables everyone references.
- When testing, does it make more sense to create a DB double, set up mock services, or write tests that leave no trace in the real DB?
- Logic around Orders join nearly every table in the database, introducing large queries and complexity. Could there be a more efficient way to design the data tables?

### OrderJdbcRepo
  - We didn't realize it when initially looking over the project, but this repo has a lot of responsibilities.
  - I wrote the methods, but in testing the App found that this repo is responsible for updating all tables associated
    with the associated Order.
  - I updated add, update, and delete to update Payment and OrderItem tables and found a lot of reused code, so I used
    chatGPT to help with refactoring and ended up being able to refactor out a lot of the reused code into private
    helper methods.
  - Now the updateOrder and addOrder use the same deleteAndInsert methods, with the delete portion of those methods
    doing nothing in addOrder because there are now rows in those tables with the new OrderID yet.
  - I also added the insertAndReturnKey method to cut down on the clutter in the addOrder method, and to get some
    practice using @FunctionalInterface, which I had read into when we went over lambda functions, but didn't have a 
    chance to use yet.

### Testing

- Unit tests were written as the Repo methods were being built to ensure best coding practices, and were occasionally updated as necessary changes were made to those methods. Unit tests were run at all stages of the project.
- Manual QA testing was conducted in the later stages of the project, with the bistro worker or manager UX in mind. Input values and boundaries were tested (but already implemented with the previous version of the app), so most testing involved CRUD operations and making sure that changes within the UI were reflected in the local DB, and vice versa.
