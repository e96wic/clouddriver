package com.netflix.spinnaker.clouddriver.cloudfoundry.controller;

import com.netflix.spinnaker.clouddriver.cloudfoundry.model.CloudFoundryCluster;
import com.netflix.spinnaker.clouddriver.cloudfoundry.provider.view.CloudFoundryClusterProvider;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

@AllArgsConstructor
@RestController
@RequestMapping("/cloudfoundry/images")
public class CloudFoundryImageController {
  private final CloudFoundryClusterProvider clusterProvider;

  /**
   * Cloud Foundry droplets aren't human readable independently of the server group
   * to which they are attached.
   */
  @RequestMapping(value = "/find", method = RequestMethod.GET)
  public Collection<CloudFoundryCluster> list(@RequestParam(required = false) String account) {
    Stream<CloudFoundryCluster> clusters = account == null ?
      clusterProvider.getClusters().values().stream().flatMap(Set::stream) :
      clusterProvider.getClusters().get("account").stream();

    return clusters
      .map(cluster -> cluster.withServerGroups(cluster.getServerGroups().stream()
        .filter(serverGroup -> serverGroup.getDroplet() != null)
        .map(serverGroup -> serverGroup.withInstances(emptySet()).withServiceInstances(emptyList()))
        .collect(toSet())))
      .filter(cluster -> !cluster.getServerGroups().isEmpty())
      .collect(toSet());
  }
}
