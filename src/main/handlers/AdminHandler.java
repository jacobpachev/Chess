package handlers;

import com.google.gson.Gson;
import services.AdminService;
import spark.Response;

public class AdminHandler {
    public Object clear(Response res) {
        Gson gson = new Gson();
        AdminService adminService = new AdminService();
        if(adminService.clear().getMessage() == null) {
            res.status(200);
            return "{}";
        }
        res.status(500);
        res.body(adminService.clear().getMessage());
        return gson.toJson(res);

    }
}
