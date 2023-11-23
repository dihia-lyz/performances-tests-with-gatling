package staffmanager;

import io.gatling.core.scenario.Scenario;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class MissionSimulation extends Simulation {
    HttpProtocolBuilder httpProtocolBuilder = http.baseUrl("http://localhost:8080");

    ChainBuilder getMissions = repeat(1).on(
            exec(http("get all missions")
                    .get("/api/v1/mission")
            ).pause(2));

    ChainBuilder updateMission = repeat(1).on(
            exec(http("update mission")
                    .put("/api/v1/mission/5")
                    .body(ElFileBody("bodies/UpdateMission.json")).asJson()
            ).pause(2));

    ScenarioBuilder scn = scenario("Test missions endpoints performances")
            .exec(getMissions)
            .exec(updateMission);

    {
        setUp(scn.injectOpen(rampUsers(40).during(5)))
                .protocols(httpProtocolBuilder);
    }
}
