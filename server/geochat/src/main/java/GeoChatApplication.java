import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import resources.*;

/**
 * Created by paulosk on 05/04/16.
 */
public class GeoChatApplication extends Application<GeoChatConfiguration> {

    public static void main(String[] args) throws Exception {
        new GeoChatApplication().run(args);
    }


    @Override
    public String getName() {
        return "GeoChat";
    }

    @Override
    public void initialize(Bootstrap<GeoChatConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(GeoChatConfiguration configuration,
                    Environment environment) {

        environment.jersey().register(new LoginResource());
        environment.jersey().register(new LogoutResource());
        environment.jersey().register(new RegisterResource());
        environment.jersey().register(new ZoneUsersResource());
        environment.jersey().register(new SendMessageResource());
        environment.jersey().register(new ReceiveMessageResource());
        environment.jersey().register(new PublishLocationResource());
        environment.jersey().register(new DatabaseConnectionTestResource());

    }
}
