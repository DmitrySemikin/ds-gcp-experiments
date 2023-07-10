package xyz.dsemikin.tmp.gcp.run_access;

import com.google.cloud.run.v2.Service;
import com.google.cloud.run.v2.ServiceName;
import com.google.cloud.run.v2.ServicesClient;
import com.google.iam.v1.*;
import com.google.protobuf.ProtocolStringList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");

        // For it to work we need to provide credentials.
        // See here: https://cloud.google.com/docs/authentication/provide-credentials-adc#how-to
        try (final ServicesClient servicesClient = ServicesClient.create()) {
            final ServiceName serviceName = ServiceName.of("dsemikin-run-java-example-001", "us-central1", "ds-spring-hello");
            final Service service = servicesClient.getService(serviceName);
            System.out.println(service);

            final GetIamPolicyRequest getIamPolicyRequest = GetIamPolicyRequest.newBuilder()
                .setResource(serviceName.toString())
                .setOptions(GetPolicyOptions.newBuilder().build())
                .build();
            final Policy oldPolicy = servicesClient.getIamPolicy(getIamPolicyRequest);
            final List<Binding> oldBindings = oldPolicy.getBindingsList();
            System.out.println("BEGIN BINDINGS");
            for (var binding : oldBindings) {
                System.out.println("Binding:");
                System.out.println(binding);
            }

            final String invokerRole = "roles/run.invoker";
            final String allUsersMember = "allUsers";
            final List<Binding> newBindings = new ArrayList<>();
            boolean bindingWasChanged = false;
            for (var oldBinding : oldBindings) {
                if (oldBinding.getRole().equals(invokerRole)) {
                    ProtocolStringList membersList = oldBinding.getMembersList();
                    if (membersList.contains(allUsersMember)) {
                        final List<String> filteredMembers = membersList.stream().filter(m -> !allUsersMember.equals(m)).toList();
                        if (!filteredMembers.isEmpty()) {
                            final Binding newBinding = Binding.newBuilder(oldBinding).clearMembers().addAllMembers(filteredMembers).build();
                            newBindings.add(newBinding);
                        }
                        // else just skip the binding
                        bindingWasChanged = true;
                    } else {
                        newBindings.add(oldBinding);
                    }
                } else {
                    newBindings.add(oldBinding);
                }
            }

            if (bindingWasChanged) {
                final Policy newPolicy = Policy.newBuilder(oldPolicy).clearBindings().addAllBindings(newBindings).build();
                final SetIamPolicyRequest setIamPolicyRequest = SetIamPolicyRequest.newBuilder()
                        .setResource(serviceName.toString())
                        .setPolicy(newPolicy)
                        .build();
                final Policy updatedPolicy = servicesClient.setIamPolicy(setIamPolicyRequest);
                System.out.println("Updated policy: ");
                System.out.println(updatedPolicy);
            } else {
                System.out.println("No bindings were changed (no binding for given role was found or it does not have given member).");
            }

            System.out.println("END BINDINGS");

        }

        System.out.println("Good Bye");
    }
}
