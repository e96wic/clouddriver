package com.netflix.spinnaker.clouddriver.cloudfoundry.deploy.ops;

import com.netflix.spinnaker.clouddriver.cloudfoundry.client.model.v3.ProcessStats;
import com.netflix.spinnaker.clouddriver.cloudfoundry.deploy.description.StopCloudFoundryServerGroupDescription;
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

class StopCloudFoundryServerGroupAtomicOperationTest extends AbstractCloudFoundryAtomicOperationTest{
  private StopCloudFoundryServerGroupDescription desc = new StopCloudFoundryServerGroupDescription();

  StopCloudFoundryServerGroupAtomicOperationTest() {
    super();
  }

  @BeforeEach
  void before() {
    desc.setClient(client);
    desc.setServerGroupName("myapp");
  }

  @Test
  void stop() {
    OperationPoller poller = mock(OperationPoller.class);

    //noinspection unchecked
    when(poller.waitForOperation(any(Supplier.class), any(), anyLong(), any(), any(), any())).thenReturn(ProcessStats.State.DOWN);

    StopCloudFoundryServerGroupAtomicOperation op = new StopCloudFoundryServerGroupAtomicOperation(poller, desc);

    assertThat(runOperation(op).getHistory())
      .has(status("Stopping 'myapp'"), atIndex(1))
      .has(status("Stopped 'myapp'"), atIndex(2));
  }

  @Test
  void failedToStop() {
    OperationPoller poller = mock(OperationPoller.class);

    //noinspection unchecked
    when(poller.waitForOperation(any(Supplier.class), any(), anyLong(), any(), any(), any())).thenReturn(ProcessStats.State.RUNNING);

    StopCloudFoundryServerGroupAtomicOperation op = new StopCloudFoundryServerGroupAtomicOperation(poller, desc);

    assertThat(runOperation(op).getHistory())
      .has(status("Stopping 'myapp'"), atIndex(1))
      .has(status("Failed to stop 'myapp' which instead is running"), atIndex(2));
  }
}