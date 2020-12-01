package choco;

import java.util.*;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class App {

    public static void main(String[] args) {
        Jdbi jdbi = Jdbi.create("jdbc:postgresql://localhost:5432/choco");
        jdbi.installPlugin(new SqlObjectPlugin());


//        jdbi.useHandle(h -> h
//                .attach(ChocoService.class)
//                .createBar("KitKat", 7)
//        );

        List<ChocolateBar> bars = jdbi.withHandle(h -> h
                .attach(ChocoService.class)
                .getBars()
        );

        for (ChocolateBar bar: bars) {
            System.out.println(bar.getName());
        }

//        List<ChocolateBar> bars = service.getBars();

//        List<ChocolateBar> list = jdbi.withHandle( h ->
//                                h.createQuery("select  * from choco")
//                                    .mapToBean(ChocolateBar.class)
//                                    .list());
//



    }

}
