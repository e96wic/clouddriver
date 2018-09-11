package com.netflix.spinnaker.clouddriver.cloudfoundry.deploy.ops;

import com.netflix.spinnaker.clouddriver.cloudfoundry.deploy.description.TerminateCloudFoundryInstancesDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TerminateCloudFoundryServerGroupAtomicOperationTest extends AbstractCloudFoundryAtomicOperationTest{
  private TerminateCloudFoundryInstancesDescription desc = new TerminateCloudFoundryInstancesDescription();

  TerminateCloudFoundryServerGroupAtomicOperationTest() {
    super();
  }

  @BeforeEach
  void before() {
    desc.setClient(client);
    desc.setInstanceIds(new String[] { "123-0", "123-1" });
  }

  @Test
  void terminate() {
    TerminateCloudFoundryInstancesAtomicOperation op = new TerminateCloudFoundryInstancesAtomicOperation(desc);

    assertThat(runOperation(op).getHistory())
      .has(status("Terminating application instances ['123-0', '123-1']"), atIndex(1))
      .has(status("Terminated application instances ['123-0', '123-1']"), atIndex(2));

    verify(applications, times(2)).deleteAppInstance(eq("123"), anyString());
  }
}