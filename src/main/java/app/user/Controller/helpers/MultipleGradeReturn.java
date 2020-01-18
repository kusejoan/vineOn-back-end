package app.user.Controller.helpers;

import app.user.Entity.WineGrade;

import java.util.ArrayList;
import java.util.List;

public class MultipleGradeReturn {

    public List<WineGradeReturn> grades;
    public boolean success;

    public MultipleGradeReturn() {}

    public MultipleGradeReturn(List<WineGrade> grades)
    {
        this.grades = new ArrayList<>();
        for(WineGrade g: grades)
        {
            this.grades.add(new WineGradeReturn(g));
        }
        this.success = true;
    }

    public List<WineGradeReturn> getGrades() {
        return grades;
    }

    public void setGrades(List<WineGradeReturn> grades) {
        this.grades = grades;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
