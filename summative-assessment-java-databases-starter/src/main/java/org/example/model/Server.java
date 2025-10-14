package org.example.model;

import java.time.LocalDate;
import java.util.Objects;

public class Server {
    private int serverID;
    private String firstName;
    private String lastName;
    private LocalDate hireDate;
    private LocalDate termDate;

    public int getServerID() {
        return serverID;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public LocalDate getTermDate() {
        return termDate;
    }

    public void setTermDate(LocalDate termDate) {
        this.termDate = termDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Server server)) return false;
        return getServerID() == server.getServerID() && Objects.equals(getFirstName(), server.getFirstName()) && Objects.equals(getLastName(), server.getLastName()) && Objects.equals(getHireDate(), server.getHireDate()) && Objects.equals(getTermDate(), server.getTermDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerID(), getFirstName(), getLastName(), getHireDate(), getTermDate());
    }

    @Override
    public String toString() {
        return "Server{" +
                "ServerID=" + serverID +
                ", FirstName='" + firstName + '\'' +
                ", LastName='" + lastName + '\'' +
                ", HireDate=" + hireDate +
                ", TermDate=" + termDate +
                '}';
    }
}
