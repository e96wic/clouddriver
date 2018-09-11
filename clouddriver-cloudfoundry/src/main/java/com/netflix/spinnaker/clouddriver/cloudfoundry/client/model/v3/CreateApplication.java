package com.netflix.spinnaker.clouddriver.cloudfoundry.client.model.v3;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class CreateApplication {
  private final String name;
  private final Map<String, ToOneRelationship> relationships;
  private final Map<String, String> environmentVariables;

  @Nullable
  private final BuildpackLifecycle lifecycle;

  public CreateApplication(String name, Map<String, ToOneRelationship> relationships, Map<String, String> environmentVariables,
                           String buildpack) {
    this.name = name;
    this.relationships = relationships;
    this.environmentVariables = environmentVariables;
    this.lifecycle = new BuildpackLifecycle(buildpack);
  }

  @AllArgsConstructor
  @Getter
  public static class BuildpackLifecycle {
    private String type = "buildpack";
    private Map<String, List<String>> data;

    BuildpackLifecycle(String buildpack) {
      this.data = Collections.singletonMap("buildpacks", singletonList(buildpack != null && buildpack.length() > 0 ? buildpack : null));
    }
  }
}