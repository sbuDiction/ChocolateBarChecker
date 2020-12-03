package choco;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChocolateServiceTest {

    Jdbi jdbi;

    @BeforeAll
    void databaseSetup() {
        jdbi = Jdbi.create("jdbc:postgresql://localhost:5432/choco");
        jdbi.installPlugin(new SqlObjectPlugin());
    }

    @BeforeEach
    void beforeEach() {
        jdbi.useHandle(h -> h.execute("delete from chocolate"));
    }

    @Test
    public void shouldBeAbleToCreateChocolateBar() {

        jdbi.useHandle(h -> {

            ChocolateService chocolateService = h.attach(ChocolateService.class);
            assertEquals(0, chocolateService.getBars().size());

            chocolateService.createBar("KitKat", 7);
            assertEquals(1, chocolateService.getBars().size());

        });

    }

    @Test
    public void shouldBeAbleToFindChocolateBarByName() {

        jdbi.useHandle(h -> {

            ChocolateService chocolateService = h.attach(ChocolateService.class);

            chocolateService.createBar("KitKat", 7);
            ChocolateBar chocolateBar = chocolateService.getBarByName("KitKat");

            assertEquals(7, chocolateBar.getQty());
            assertEquals("KitKat", chocolateBar.getName());

        });

    }


}
