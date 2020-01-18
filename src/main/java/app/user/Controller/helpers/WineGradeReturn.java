package app.user.Controller.helpers;

import app.user.Entity.WineGrade;

public class WineGradeReturn {
    public String username;
    public String wineName;
    public Long grade;
    public String description;

    public WineGradeReturn (String user, String wineName, Long grade, String description) {
        this.username = user;
        this.wineName = wineName;
        this.grade = grade;
        this.description = description;
    }

    public WineGradeReturn(WineGrade grade)
    {
        this.username = grade.getUser().getUsername();
        this.wineName = grade.getWine().getWineName();
        this.grade = grade.getGrade();
        this.description = grade.getDescription();

    }


    public String getUser() {
        return username;
    }

    public void setUser(String user) {
        this.username = user;
    }

    public String getWineName() {
        return wineName;
    }

    public void setWineName(String wineName) {
        this.wineName = wineName;
    }

    public Long getGrade() {
        return grade;
    }

    public void setGrade(Long grade) {
        this.grade = grade;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
