package integration.grid;

import com.codeborne.selenide.Configuration;
import integration.IntegrationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.grid.selenium.GridLauncher;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.close;
import static org.openqa.selenium.net.PortProber.findFreePort;

public class SeleniumGridTest extends IntegrationTest {
  @Before
  public void setUp() throws Exception {
    close();
    
    int hubPort = findFreePort();
    GridLauncher.main(new String[]{"-port", "" + hubPort, "-host", "localhost"});

    GridLauncher.main(new String[]{"-port", "" + findFreePort(),
        "-host", "localhost",
        "-role", "node",
        "-hub", "http://localhost:" + hubPort + "/grid/register"
    });

    Configuration.remote = "http://localhost:" + hubPort + "/wd/hub";
  }

  @Test
  public void canUseSeleniumGrid() {
    openFile("page_with_selects_without_jquery.html");
    $$("#radioButtons input").shouldHave(size(4));
  }

  @After
  public void tearDown() {
    close();
    Configuration.remote = null;
  }
}
