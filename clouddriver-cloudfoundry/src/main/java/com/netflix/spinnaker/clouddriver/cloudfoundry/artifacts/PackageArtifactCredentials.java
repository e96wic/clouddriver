package com.netflix.spinnaker.clouddriver.cloudfoundry.artifacts;

import com.netflix.spinnaker.clouddriver.artifacts.config.ArtifactCredentials;
import com.netflix.spinnaker.clouddriver.cloudfoundry.client.CloudFoundryClient;
import com.netflix.spinnaker.kork.artifacts.model.Artifact;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.InputStream;
import java.util.List;

import static java.util.Collections.singletonList;

@AllArgsConstructor
@Getter
public class PackageArtifactCredentials implements ArtifactCredentials {
  private final String name = "package";
  private final CloudFoundryClient client;

  @Override
  public List<String> getTypes() {
    return singletonList("package");
  }

  @Override
  public InputStream download(Artifact artifact) {
    String packageId = client.getApplications().findCurrentPackageIdByAppId(artifact.getReference());
    return client.getApplications().downloadPackageBits(packageId);
  }
}
