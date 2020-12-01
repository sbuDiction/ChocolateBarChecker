package choco;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface ChocoService {


    @SqlQuery("select * from choco")
    @RegisterBeanMapper(ChocolateBar.class)
    List<ChocolateBar> getBars();


    @SqlUpdate("insert into choco (name, qty) values (?, ?)")
    void createBar(String name, int qty);

}
