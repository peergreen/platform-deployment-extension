/**
 * Copyright 2013 Peergreen S.A.S. All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.peergreen.deployment.urls.processor;

import com.peergreen.deployment.ProcessorContext;
import com.peergreen.deployment.ProcessorException;
import com.peergreen.deployment.processor.Phase;
import com.peergreen.deployment.processor.Processor;
import com.peergreen.deployment.urls.UrlsDeploymentPlan;
import com.peergreen.deployment.urls.processor.delta.Delta;

/**
 * @author Mohammed Boukada
 */
@Processor
@Phase("urls-update")
public class UrlsUpdateProcessor {

    public void handle(Deltas deltas, ProcessorContext processorContext) throws ProcessorException {
        UrlsDeploymentPlan deploymentPlan = processorContext.getArtifact().as(UrlsDeploymentPlan.class);
        for (Delta delta : deltas) {
            switch (delta.getKind()) {
                case ADDED:
                    deploymentPlan.add(delta.getActual());
                    // TODO add artifact and deploy it
                    break;
                case REMOVED:
                    deploymentPlan.remove(delta.getPrevious().uri());
                    // TODO remove artifact and undeploy it
                    break;
                case UNCHANGED:
                    // do nothing
            }
        }
    }
}
