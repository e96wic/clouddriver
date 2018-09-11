package com.netflix.spinnaker.clouddriver.cloudfoundry.deploy.ops;

import com.netflix.spinnaker.clouddriver.cloudfoundry.client.model.v3.ProcessStats;

class CloudFoundryOperationUtils {
  static String describeProcessState(ProcessStats.State state) {
    switch(state) {
      case STARTING:
        return "is still starting";
      case CRASHED:
        return "crashed";
      case RUNNING:
      case DOWN:
      default:
        return "is " + state.toString().toLowerCase();
    }
  }
}
