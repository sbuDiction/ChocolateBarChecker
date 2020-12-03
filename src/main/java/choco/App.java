package choco;

import com.google.gson.Gson;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import static spark.Spark.*;

import java.util.List;

public class App {

    public static void main(String[] args) {
        Jdbi jdbi = Jdbi.create("jdbc:postgresql://localhost:5432/choco");
        jdbi.installPlugin(new SqlObjectPlugin());

        staticFiles.location("/public"); // Static files

        Gson gson = new Gson();

        get("/api/chocolates", (req, res) -> {

            List<ChocolateBar> bars = jdbi.withHandle(h -> {
                ChocolateService chocolateService = h.attach(ChocolateService.class);
                return chocolateService.getBars();
            });

            return bars;

        }, gson::toJson);




    }

}
