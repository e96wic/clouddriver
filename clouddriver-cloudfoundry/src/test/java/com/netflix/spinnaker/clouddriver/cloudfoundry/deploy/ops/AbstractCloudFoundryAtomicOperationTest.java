package com.netflix.spinnaker.clouddriver.cloudfoundry.deploy.ops;

import com.netflix.spinnaker.clouddriver.cloudfoundry.client.*;
import com.netflix.spinnaker.clouddriver.cloudfoundry.client.model.v3.ProcessStats;
import com.netflix.spinnaker.clouddriver.data.task.DefaultTask;
import com.netflix.spinnaker.clouddriver.data.task.Status;
import com.netflix.spinnaker.clouddriver.data.task.Task;
import com.netflix.spinnaker.clouddriver.data.task.TaskRepository;
import com.netflix.spinnaker.clouddriver.orchestration.AtomicOperation;
import org.assertj.core.api.Condition;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AbstractCloudFoundryAtomicOperationTest {
  final CloudFoundryClient client;
  final Routes routes;
  final Spaces spaces;
  final Domains domains;
  final Applications applications;

  AbstractCloudFoundryAtomicOperationTest() {
    client = mock(CloudFoundryClient.class);
    routes = mock(Routes.class);
    spaces = mock(Spaces.class);
    domains = mock(Domains.class);
    applications = mock(Applications.class);

    when(client.getRoutes()).thenReturn(routes);
    when(client.getSpaces()).thenReturn(spaces);
    when(client.getDomains()).thenReturn(domains);
    when(client.getApplications()).thenReturn(applications);
  }

  Task runOperation(AtomicOperation<?> op) {
    Task task = new DefaultTask("test");
    TaskRepository.threadLocalTask.set(task);
    op.operate(emptyList());
    return task;
  }

  static Condition<? super Status> status(String desc) {
    return new Condition<>(status -> status.getStatus().equals(desc), "description = '" + desc + "'");
  }
}
