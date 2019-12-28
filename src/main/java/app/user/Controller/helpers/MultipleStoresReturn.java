package app.user.Controller.helpers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MultipleStoresReturn {
    private List<StoreReturn> stores;
    private boolean success;

    public MultipleStoresReturn() {
        this.stores = new ArrayList<>();
    }

    public MultipleStoresReturn(List<StoreReturn> stores) {
        this.stores = stores;
    }

    public List<StoreReturn> getStores() {
        return stores;
    }

    public void setStores(List<StoreReturn> stores) {
        this.stores = stores;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
