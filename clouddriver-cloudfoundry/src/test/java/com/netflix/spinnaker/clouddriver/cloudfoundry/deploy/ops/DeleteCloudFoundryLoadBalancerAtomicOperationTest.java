package com.netflix.spinnaker.clouddriver.cloudfoundry.deploy.ops;

import com.netflix.spinnaker.clouddriver.cloudfoundry.deploy.description.DeleteCloudFoundryLoadBalancerDescription;
import com.netflix.spinnaker.clouddriver.cloudfoundry.model.CloudFoundryDomain;
import com.netflix.spinnaker.clouddriver.cloudfoundry.model.CloudFoundryLoadBalancer;
import com.netflix.spinnaker.clouddriver.data.task.Task;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Index.atIndex;
import static org.mockito.Mockito.verify;

class DeleteCloudFoundryLoadBalancerAtomicOperationTest extends AbstractCloudFoundryAtomicOperationTest {
  @Test
  void deleteLoadBalancer() {
    CloudFoundryLoadBalancer loadBalancer = CloudFoundryLoadBalancer.builder()
      .id("id")
      .host("host")
      .domain(CloudFoundryDomain.builder().name("mydomain").build())
      .build();

    DeleteCloudFoundryLoadBalancerDescription desc = new DeleteCloudFoundryLoadBalancerDescription();
    desc.setClient(client);
    desc.setLoadBalancer(loadBalancer);

    DeleteCloudFoundryLoadBalancerAtomicOperation op = new DeleteCloudFoundryLoadBalancerAtomicOperation(desc);
    Task task = runOperation(op);

    assertThat(task.getHistory())
      .has(status("Deleting load balancer " + loadBalancer.getName()), atIndex(1))
      .has(status("Deleted load balancer " + loadBalancer.getName()), atIndex(2));

    verify(routes).deleteRoute(loadBalancer.getId());
  }
}