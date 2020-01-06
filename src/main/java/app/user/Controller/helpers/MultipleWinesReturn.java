package app.user.Controller.helpers;

import app.user.Entity.Wine;

import java.util.List;

public class MultipleWinesReturn {
    public MultipleWinesReturn() {}

    public MultipleWinesReturn(List<Wine> wines) {
        this.wines = wines;
    }

    private List<Wine> wines;
    private boolean success;

    public List<Wine> getWines() {
        return wines;
    }

    public void setWines(List<Wine> wines) {
        this.wines = wines;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}