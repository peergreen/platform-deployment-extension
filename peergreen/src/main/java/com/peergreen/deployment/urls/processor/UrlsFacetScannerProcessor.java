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

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.felix.ipojo.annotations.Requires;

import com.peergreen.deployment.Artifact;
import com.peergreen.deployment.DiscoveryPhasesLifecycle;
import com.peergreen.deployment.ProcessorContext;
import com.peergreen.deployment.ProcessorException;
import com.peergreen.deployment.facet.content.Content;
import com.peergreen.deployment.facet.content.ContentException;
import com.peergreen.deployment.processor.Discovery;
import com.peergreen.deployment.processor.Processor;
import com.peergreen.deployment.processor.Uri;
import com.peergreen.deployment.urls.UrlsDeploymentPlan;
import com.peergreen.deployment.urls.processor.parser.UrlsDeploymentPlanParser;

/**
 * @author Mohammed Boukada
 */
@Processor
@Discovery(DiscoveryPhasesLifecycle.FACET_SCANNER)
@Uri(extension = "urls")
public class UrlsFacetScannerProcessor {

    private UrlsDeploymentPlanParser parser;

    public UrlsFacetScannerProcessor(@Requires UrlsDeploymentPlanParser parser) {
        this.parser = parser;
    }

    /**
     * Needs to parse the file content which is the urls deployment plan.
     */
    public void handle(Content content, ProcessorContext processorContext) throws ProcessorException {
        try {
            UrlsDeploymentPlan deploymentPlan = parser.parse(content.getInputStream());
            for (Artifact artifact : deploymentPlan.getArtifacts()) {
                processorContext.addArtifact(artifact);
            }
            processorContext.addFacet(UrlsDeploymentPlan.class, deploymentPlan);
        } catch (IOException e) {
            throw new ProcessorException("Unable to read the file", e);
        } catch (URISyntaxException e) {
            throw new ProcessorException("Unable to build URI", e);
        } catch (ContentException e) {
            throw new ProcessorException("Unable to get input stream", e);
        }
    }
}
