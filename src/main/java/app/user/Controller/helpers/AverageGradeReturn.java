package app.user.Controller.helpers;

public class AverageGradeReturn {
    public double grade;
    public int amountOfGrades;
    public boolean success;
    public String message;

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getAmountOfGrades() {
        return amountOfGrades;
    }

    public void setAmountOfGrades(int amountOfGrades) {
        this.amountOfGrades = amountOfGrades;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
