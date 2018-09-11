package com.netflix.spinnaker.clouddriver.cloudfoundry.deploy.ops;

import com.netflix.spinnaker.clouddriver.cloudfoundry.client.model.v3.ProcessStats;
import com.netflix.spinnaker.clouddriver.cloudfoundry.deploy.description.StartCloudFoundryServerGroupDescription;
import com.netflix.spinnaker.clouddriver.helpers.OperationPoller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StartCloudFoundryServerGroupAtomicOperationTest extends AbstractCloudFoundryAtomicOperationTest{
  private StartCloudFoundryServerGroupDescription desc = new StartCloudFoundryServerGroupDescription();

  StartCloudFoundryServerGroupAtomicOperationTest() {
    super();
  }

  @BeforeEach
  void before() {
    desc.setClient(client);
    desc.setServerGroupName("myapp");
  }

  @Test
  void start() {
    OperationPoller poller = mock(OperationPoller.class);

    //noinspection unchecked
    when(poller.waitForOperation(any(Supplier.class), any(), anyLong(), any(), any(), any())).thenReturn(ProcessStats.State.RUNNING);

    StartCloudFoundryServerGroupAtomicOperation op = new StartCloudFoundryServerGroupAtomicOperation(poller, desc);

    assertThat(runOperation(op).getHistory())
      .has(status("Starting 'myapp'"), atIndex(1))
      .has(status("Started 'myapp'"), atIndex(2));
  }

  @Test
  void failedToStart() {
    OperationPoller poller = mock(OperationPoller.class);

    //noinspection unchecked
    when(poller.waitForOperation(any(Supplier.class), any(), anyLong(), any(), any(), any())).thenReturn(ProcessStats.State.CRASHED);

    StartCloudFoundryServerGroupAtomicOperation op = new StartCloudFoundryServerGroupAtomicOperation(poller, desc);

    assertThat(runOperation(op).getHistory())
      .has(status("Starting 'myapp'"), atIndex(1))
      .has(status("Failed to start 'myapp' which instead crashed"), atIndex(2));
  }
}