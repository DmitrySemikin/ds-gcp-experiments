package xyz.dsemikin.tmp.gcp.run_access;

import com.google.cloud.run.v2.Service;
import com.google.cloud.run.v2.ServiceName;
import com.google.cloud.run.v2.ServicesClient;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");

        // For it to work we need to provide credentials.
        // See here: https://cloud.google.com/docs/authentication/provide-credentials-adc#how-to
        try (final ServicesClient servicesClient = ServicesClient.create()) {
            final ServiceName name = ServiceName.of("dsemikin-run-java-example-001", "us-central1", "ds-spring-hello");
            final Service service = servicesClient.getService(name);
            System.out.println(service);
        }

        System.out.println("Good Bye");
    }
}
