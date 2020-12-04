package choco;

import com.google.gson.Gson;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

    public static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }

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

        get("/", (req, res) -> {

            List<ChocolateBar> bars = jdbi.withHandle(h -> {
                ChocolateService chocolateService = h.attach(ChocolateService.class);
                return chocolateService.getBars();
            });

            Map paramsMap = new HashMap();
            paramsMap.put("chocolates", bars);

            return render(paramsMap, "chocolates.handlebars");

        });

        get("/add", (req, res) -> {

            Map paramsMap = new HashMap();
            return render(paramsMap, "add.handlebars");

        });

        post("/add", (req, res) -> {

            String name = req.queryParams("name");

            String qtyString = req.queryParams("qty");

            int  qty = Integer.parseInt(qtyString);

            if  (name.equals("") || qty == 0) {
                res.redirect("/");
            }


            jdbi.useHandle(h -> {
                ChocolateService chocolateService = h.attach(ChocolateService.class);
                chocolateService.createBar(name, qty);
            });

            res.redirect("/");
            return null;
        });

        post("/chocolate_stock", (req, res) -> {

            int chocolateId = Integer.parseInt(req.queryParams("id"));

            jdbi.useHandle(h -> {

                ChocolateService chocolateService = h.attach(ChocolateService.class);

                System.out.println(chocolateId);


                if (req.queryMap("plus").hasValue()) {
                    chocolateService.incrementQty(chocolateId);
                }
                else if (req.queryMap("minus").hasValue()) {
                    chocolateService.decrementQty(chocolateId);
                }

                if (chocolateService.getBarById(chocolateId).getQty() == 0) {
                    chocolateService.deleteBar(chocolateId);
                }

            });


            res.redirect("/");
            return null;
        });







    }

}
