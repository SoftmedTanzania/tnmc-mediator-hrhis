package tz.go.moh.him.tnmc.mediator.hrhis;

import akka.actor.Props;
import org.openhim.mediator.engine.testing.MockLauncher;

import java.util.ArrayList;
import java.util.List;

public class CustomMockLauncher extends MockLauncher {

    public CustomMockLauncher(List<ActorToLaunch> actorsToLaunch) {
        super(actorsToLaunch);
    }

    public CustomMockLauncher(Class<Object> aClass, String name) {
        super(new ArrayList<>());
        this.getContext().actorOf(Props.create(aClass), name);
    }

}