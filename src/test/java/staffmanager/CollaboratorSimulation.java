package staffmanager;


import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class CollaboratorSimulation extends Simulation {
    HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8080");

    ChainBuilder getAllCollaborators = repeat(1).on(exec(http("Récupération des collabs")
            .get("/api/v1/collaborator"))
            .pause(2));

    ChainBuilder createCollaborator = repeat(1).on(exec(http("creation d'un nouveau collab")
            .post("/api/v1/collaborator")
            .body(ElFileBody("bodies/NewCollaborator.json"))
            .asJson())
            .pause(2));

    ChainBuilder updateCollaborator = repeat(1).on(exec(http("update collab")
            .put("/api/v1/collaborator/6")
            .body(ElFileBody("bodies/UpdateCollaborator.json"))
            .asJson())
            .pause(2));

    ScenarioBuilder getCollaborators = scenario("Scenario : Tests de performances des Endpoints de Staff-Manager-API -> collaborators")
            .exec(getAllCollaborators)
            .exec(createCollaborator)
            .exec(updateCollaborator);

    {
        setUp(getCollaborators.injectOpen(rampUsers(40).during(5))).protocols(httpProtocol);
    }

}
